package com.example.SocialNetwork.service.notification;


import com.example.SocialNetwork.config.JWTDecoder;
import com.example.SocialNetwork.entity.Post;
import com.example.SocialNetwork.entity.User;
import com.example.SocialNetwork.entity.group.Group;
import com.example.SocialNetwork.entity.notification.Notification;
import com.example.SocialNetwork.repository.notification.NotificationRepo;
import com.example.SocialNetwork.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    Map<String, Sinks.Many<Notification>> notificationSinks = new ConcurrentHashMap<>();
    NotificationRepo notificationRepo;
    UserService userService;
    JWTDecoder jwtDecoder;

    public Flux<Notification> getNotificationFlux(String token) {
        String username = jwtDecoder.extractUsernameFromToken(token);
        if (username == null) {
            return Flux.error(new IllegalArgumentException("Token không hợp lệ hoặc hết hạn"));
        }
        Sinks.Many<Notification> sink = notificationSinks.computeIfAbsent(username,
                usn -> Sinks.many().multicast().directBestEffort());
        return sink.asFlux()
                .doOnCancel(() -> notificationSinks.remove(username));
    }



    public void notifyGroupRequest(User requestor, User recipient, Group group) {
        String url = String.format("/group-detail/%s?tab=request", group.getId());
        String content = requestor.getFullName() + " đã gửi yêu cầu tham gia nhóm " + group.getName();
        createNotification(recipient, content, url);
    }

    public void notifyActionGroupRequest(User recipient, Group group, boolean accept) {
        String url =  "/group-detail/" + group.getId();
        String action = accept ? "được chấp nhận" : "bị từ chối";
        String content = String.format("Yêu cầu tham gia nhóm %s của bạn đã %s.", group.getName(), action);
        createNotification(recipient, content, url);
    }

    public void notifyAcceptPost(User requestor, Post post) {
        String url = "/group-detail/" + post.getGroup().getId();
        String content = String.format("Bài viết trong nhóm %s của bạn đã được duyệt.", post.getGroup().getName());
        createNotification(post.getAuthor(), content, url);
    }

    public void sendPost(User requestor, User recipient, Group group) {
        String url = "/group-detail/" + group.getId();
        String content = requestor.getFullName() + " đã gửi 1 bài viết cần phê duyệt";
        createNotification(recipient, content, url);
    }


    private void createNotification(User recipient, String content, String url) {
        Notification notification = Notification.builder()
                .content(content)
                .recipient(recipient.getUsername())
                .navigateUrl(url)
                .isRead(false)
                .time(LocalDateTime.now())
                .build();
        notificationRepo.save(notification);
        Mono.delay(Duration.ofSeconds(1))
                .doOnSuccess(ignored ->
                        Optional.ofNullable(notificationSinks.get(recipient.getUsername()))
                                .ifPresent(n -> n.tryEmitNext(notification)))
                .subscribe();
    }

    public List<Notification> getMyNotifications(Long lastId) {
        User requestor = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(0, 10);
        return notificationRepo.findByRecipient(requestor.getUsername(), lastId, pageable);
    }

    public void markAsRead(Long id) {
        notificationRepo.findById(id)
                .ifPresent(n -> {
                    n.setRead(true);
                    notificationRepo.save(n);
                });
    }

    public Long getUnreadTotal() {
        User requestor = userService.getCurrentUser();
        return notificationRepo.getUnreadTotal(requestor.getUsername());
    }

}

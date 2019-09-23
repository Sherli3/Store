package store.configuration.registration;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import store.configuration.model.User;
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private String appUrl;
	private Locale locale;
	private User user;

	public OnRegistrationCompleteEvent(final User user, final Locale locale, final String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}
	

}

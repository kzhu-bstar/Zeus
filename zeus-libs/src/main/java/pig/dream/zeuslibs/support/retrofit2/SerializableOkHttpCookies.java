package pig.dream.zeuslibs.support.retrofit2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

public class SerializableOkHttpCookies implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient final Cookie cookie;
	private transient Cookie clientCookie;

	public SerializableOkHttpCookies(Cookie cookies) {
		this.cookie = cookies;
	}

	public Cookie getCookies() {
		Cookie bestCookies = cookie;
		if (clientCookie != null) {
			bestCookies = clientCookie;
		}
		return bestCookies;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(cookie.name());
		out.writeObject(cookie.value());
		out.writeLong(cookie.expiresAt());
		out.writeObject(cookie.domain());
		out.writeObject(cookie.path());
		out.writeBoolean(cookie.secure());
		out.writeBoolean(cookie.httpOnly());
		out.writeBoolean(cookie.hostOnly());
		out.writeBoolean(cookie.persistent());
	}

	@SuppressWarnings("unused")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String name = (String) in.readObject();
		String value = (String) in.readObject();
		long expiresAt = in.readLong();
		String domain = (String) in.readObject();
		String path = (String) in.readObject();
		boolean secure = in.readBoolean();
		boolean httpOnly = in.readBoolean();
		boolean hostOnly = in.readBoolean();
		boolean persistent = in.readBoolean();
		Cookie.Builder builder = new Cookie.Builder();
		builder = builder.name(name);
		builder = builder.value(value);
		builder = builder.expiresAt(expiresAt);
		builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
		builder = builder.path(path);
		builder = secure ? builder.secure() : builder;
		builder = httpOnly ? builder.httpOnly() : builder;
		clientCookie = builder.build();
	}
}
package hwolf.spring.boot.jsf;

import java.net.URL;

import javax.faces.application.ViewResource;

final class ViewResourceImpl extends ViewResource {

    private final URL url;

    ViewResourceImpl(URL url) {
        this.url = url;
    }

    @Override
    public URL getURL() {
        return url;
    }
}
package com.view.lift.calmmeditation.dto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

@Root(name = "content", strict = false)
public class MediaContent {


    @Attribute(name = "url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Attribute(name = "duration")
    private String duration;

    public String getUrl() {
        return url;
    }

    public String getDuration() {
        return duration;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Element(name = "thumbnail", required = false)
    private Thumbnail thumbnail;

    @Root(name = "thumbnail", strict = false)
    public static class Thumbnail {

        public String getUrl() {
            return url;
        }

        @Attribute(name = "url")
        private String url;
    }

}

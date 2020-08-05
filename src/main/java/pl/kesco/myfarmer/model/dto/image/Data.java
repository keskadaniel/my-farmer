package pl.kesco.myfarmer.model.dto.image;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Data {

    private String id;
    private String title;
    private String description;
    private BigInteger datetime;
    private String type;
    private boolean animated;
    private Integer width;
    private Integer height;
    private Integer size;
    private Integer views;
    private Integer bandwidth;
    private String vote;
    private boolean favorite;
    private String nsfw;
    private String section;
    private String account_url;
    private Integer account_id;
    private String is_ad;
    private boolean in_most_viral;
    private boolean has_sound;
    private String[] tags;
    private Integer ad_type;
    private String ad_url;
    private String edited;
    private boolean in_gallery;
    private String deletehash;
    private String name;
    private String link;
    private String hls;
    private String mp4;

}

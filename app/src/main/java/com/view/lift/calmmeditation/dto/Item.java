package com.view.lift.calmmeditation.dto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

/**
 * @author Anirudh Sharma
 *
 * The 'Article' class represents one single article and only stores the title, link and description of it.
 */
@Root(name = "item", strict = false)
public class Item {

    @Element(name = "title")
    private String title;

    @Element(name = "link")
    private String link;

    public String getCategory() {
        return category;
    }

    //@Element(name = "category", required = false )
    //@Path("category")
    //@Order
    private String category;


    public String getCategory2() {
        return category2;
    }


    public List<String> getCategoryList() {
        return categoryList;
    }

    @ElementList(entry = "category", inline = true)
    private List<String> categoryList;

    //@Root(name = "media:category", strict = true)
    public class Category{


        public String getCategory() {
            return category;
        }

        String category;

    }

    //@Path("category")
    //@Text(required=true)
    //@Order
    private String category2;

    @Element(name = "description")
    private String description;

    public MediaContent getMediaContent() {
        return mediaContent;
    }



    @Element(name = "content")
    private MediaContent mediaContent;


    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

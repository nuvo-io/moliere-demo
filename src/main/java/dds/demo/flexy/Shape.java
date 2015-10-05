package dds.demo.flexy;

import com.prismtech.cafe.dcps.keys.KeyList;

@KeyList ( topicType = "dds.demo.flexy.Shape",  keys = {"color"})
public class Shape {
    public String color;
    public int x;
    public int y;
    public int size;

    public Shape(String s, int a, int b, int c) {
        this.color = s;
        this.x = a;
        this.y = b;
        this.size = c;
    }

}

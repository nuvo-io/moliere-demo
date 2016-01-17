package dds.demo.flexy;

import com.prismtech.cafe.dcps.keys.KeyList;


@KeyList ( topicType = "dds.demo.flexy.Point",  keys = {})
public class Point {
    public int x;
    public int y;
    public Point(int a, int b) {
        this.x = a;
        this.y = b;
    }
}

package dds.demo.play;


/**
* dds/demo/play/Coord2DListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/ddsplay.idl
* Monday, January 12, 2015 5:31:22 PM CET
*/

public final class Coord2DListHolder implements org.omg.CORBA.portable.Streamable
{
  public dds.demo.play.Coord2D value[] = null;

  public Coord2DListHolder ()
  {
  }

  public Coord2DListHolder (dds.demo.play.Coord2D[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = dds.demo.play.Coord2DListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    dds.demo.play.Coord2DListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return dds.demo.play.Coord2DListHelper.type ();
  }

}

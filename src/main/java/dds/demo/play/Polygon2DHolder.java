package dds.demo.play;

/**
* dds/demo/play/Polygon2DHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/ddsplay.idl
* Monday, January 12, 2015 5:31:22 PM CET
*/

public final class Polygon2DHolder implements org.omg.CORBA.portable.Streamable
{
  public dds.demo.play.Polygon2D value = null;

  public Polygon2DHolder ()
  {
  }

  public Polygon2DHolder (dds.demo.play.Polygon2D initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = dds.demo.play.Polygon2DHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    dds.demo.play.Polygon2DHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return dds.demo.play.Polygon2DHelper.type ();
  }

}
package dds.demo;

/**
* dds/demo/PlethHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/oximeter.idl
* Thursday, 30 May 2013 18:45:58 o'clock CEST
*/

public final class PlethHolder implements org.omg.CORBA.portable.Streamable
{
  public dds.demo.Pleth value = null;

  public PlethHolder ()
  {
  }

  public PlethHolder (dds.demo.Pleth initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = dds.demo.PlethHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    dds.demo.PlethHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return dds.demo.PlethHelper.type ();
  }

}

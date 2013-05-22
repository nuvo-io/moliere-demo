package dds.demo;


/**
* dds/demo/Pleth.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/oximeter.idl
* Monday, May 20, 2013 10:59:38 AM CEST
*/

/**
* Updated by idl2j
* from ../idl/oximeter.idl
* Monday, May 20, 2013 10:59:39 AM CEST
*/

import org.opensplice.mobile.dcps.keys.KeyList;

@KeyList(
    topicType = "Pleth",
    keys = {"deviceId"}
)
public final class Pleth implements org.omg.CORBA.portable.IDLEntity
{
  public String deviceId = null;
  public float pleth = (float)0;

  public Pleth ()
  {
  } // ctor

  public Pleth (String _deviceId, float _pleth)
  {
    deviceId = _deviceId;
    pleth = _pleth;
  } // ctor

} // class Pleth

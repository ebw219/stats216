package edu.lehigh.cse216.lyle.backend;

import java.io.IOException;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.auth.AuthDescriptor;


public class Memcachier {




    public static void memc(String s) {


        //memcachier
        AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                new PlainCallbackHandler( "C7641F",
               "C4C15F74D71B642C11158E70D782AD32"));

        try {
            MemcachedClient mc = new MemcachedClient(new ConnectionFactoryBuilder()
                    .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                    .setAuthDescriptor(ad).build(), AddrUtil.getAddresses( "mc1.dev.ec2.memcachier.com:11211"));
            mc.set("foo", 1, s);
            System.out.println(mc.get("foo"));
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: \nIOException "
                    + ioe.getMessage());
        }

    }




}
package com.example.systemlabs1.test;


import android.os.AsyncTask;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * Created by systemlabs1 on 28/08/15.
 */
public class SSHHelper extends AsyncTask<String,Integer,String>{
    private JSch _jsch;
    private Session _session;




    public void writeData(String val){
        try {
            _session.connect();
            ChannelExec channelssh = (ChannelExec)
                    _session.openChannel("exec");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);

            // Execute command
            String command = "echo " + val + " >> " + "abc.txt";
            System.out.println("Angle" + val);
            channelssh.setCommand(command);
            channelssh.connect();
            channelssh.disconnect();
        }
        catch(Exception ee)
        {
            System.out.println(ee.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            _jsch = new JSch();
            _session = _jsch.getSession(params[0], params[1], 22);
            _session.setPassword(params[3]);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            _session.setConfig(prop);

            _session.connect();
        }
        catch(Exception ee)
        {

        }
        return null;
    }
}

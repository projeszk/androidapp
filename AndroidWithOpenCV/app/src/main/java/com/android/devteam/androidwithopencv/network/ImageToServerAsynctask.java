package com.android.devteam.androidwithopencv.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.android.devteam.androidwithopencv.ResultCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Szanyi Gabor
 */
public class ImageToServerAsynctask extends AsyncTask<Void, Void, Bitmap>{

    Socket socket;
    File image;
    OutputStream outputStream;
    InputStream inputStream;
    static ByteArrayOutputStream imagetoByte;
    DataOutputStream dos;
    public static String HOST = "";
    private static final int PORT = 8080;
    int choice;
    ResultCallback rc;

    public ImageToServerAsynctask(File image, int choice, ResultCallback rc) {
        this.image = image;
        this.choice = choice;
        this.rc = rc;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        Socket client = null;
        try {
            client = new Socket(HOST, PORT);
            System.out.println("HOSTNAME" + HOST);

            byte[] bs;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,240,320,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            bs = stream.toByteArray();
            System.out.println("Byte array length: " + bs.length);

            BufferedInputStream input = new BufferedInputStream(new FileInputStream(image));
            BufferedOutputStream out = null;
            BufferedInputStream recieve = new BufferedInputStream(client.getInputStream());
            try {
                out = new BufferedOutputStream(client.getOutputStream());
                final int buffSize = 1024;
                int chuncks = bs.length / 1024;
                System.out.println("chuncks " + chuncks);
                int i = 0;
                while (i<chuncks) {
                    System.out.println("Sending " + i + ". package");
                    out.write(Arrays.copyOfRange(bs, i * buffSize, i * buffSize + buffSize), 0, buffSize);
                    out.flush();
                    System.out.println(recieve.read());
                    ++i;
                }
                out.write(Arrays.copyOfRange(bs, i*buffSize, i*buffSize+(bs.length - i*buffSize - 1)), 0, (bs.length - i*buffSize - 1));
                out.flush();
                System.out.println(recieve.read());
                out.write("ok".getBytes(), 0, 2);
                out.flush();
            } catch (Exception e) {
                System.out.println("Exception occured while sending image " + e.getMessage());
            }

            try {
                System.out.println("Reading Image");
                System.out.println(client.getReceiveBufferSize());
                byte[] sizeInBytes = new byte[4];
                recieve.read(sizeInBytes, 0, 4);
                ByteBuffer wrapped = ByteBuffer.wrap(sizeInBytes);
                int size = wrapped.getInt();
                System.out.println(size);
                byte[] data = new byte[size];

                recieve.read(data, 0, size);
                Bitmap bmp = BitmapFactory.decodeByteArray(data , 0, data.length);
                if(bmp == null){
                    System.out.println("Error receiving image");
                }
                return BitmapFactory.decodeByteArray(data , 0, data.length);
           } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
            }

        } catch (IOException exception) {
            System.err.println("Exception: " + exception.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        rc.onResultReceived(result);
    }
}

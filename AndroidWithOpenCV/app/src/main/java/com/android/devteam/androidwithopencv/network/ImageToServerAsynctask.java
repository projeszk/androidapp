package com.android.devteam.androidwithopencv.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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

/**
 * Created by Szanyi Gabor
 */
public class ImageToServerAsynctask extends AsyncTask<Void, Void, Bitmap> {

    Socket socket;
    File image;
    OutputStream outputStream;
    InputStream inputStream;
    static ByteArrayOutputStream imagetoByte;
    DataOutputStream dos;
    public static String HOST = "";
    private static final int PORT = 8080;
    int choice;

    public ImageToServerAsynctask(File image, int choice) {
        this.image = image;
        this.choice = choice;
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
            bs= stream.toByteArray();

            System.out.println("Byte array length: " + bs.length);

            BufferedInputStream input = new BufferedInputStream(new FileInputStream(image));
            BufferedOutputStream out = null;
            BufferedInputStream recieve = new BufferedInputStream(client.getInputStream());
            try {
                out = new BufferedOutputStream(client.getOutputStream());
                final int buffSize = 1024;
                byte[] buffer = new byte[buffSize];
                int size;
                while ((size = input.read(buffer)) != -1) {
                    System.out.println("data: " + size);
                    out.write(buffer, 0, size);
                    out.flush();
                    System.out.println(recieve.read());
                }
                out.write("ok".getBytes(), 0, 2);
                out.flush();
            } catch (Exception e) {
                System.out.println("Exception occured while sending image " + e.getMessage());
            }

            try {
                byte[] b = new byte[client.getReceiveBufferSize()];
                recieve.read(b);

                byte[] data;

                System.out.println("Reading Image");

                data = new byte[client.getReceiveBufferSize()];

                recieve.read(data, 0, client.getReceiveBufferSize());
                return BitmapFactory.decodeByteArray(data , 0, data .length);
           } catch (Exception e) {System.err.println(e.getMessage());}

        } catch (IOException exception) {
            System.err.println("Exception: " + exception.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }

}

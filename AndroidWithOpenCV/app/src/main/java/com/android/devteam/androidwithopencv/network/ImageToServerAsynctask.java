package com.android.devteam.androidwithopencv.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.android.devteam.androidwithopencv.ResultCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
            System.out.println("HOSTNAME " + HOST);

            byte[] bs;
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            bitmap = Bitmap.createScaledBitmap(bitmap,640,480,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bs = stream.toByteArray();

            BufferedOutputStream out = null;
            DataInputStream receive = new DataInputStream(client.getInputStream());
            try {
                out = new BufferedOutputStream(client.getOutputStream());
                final int buffSize = 1024;
                int chunks = bs.length / 1024;
                System.out.println("chuncks " + chunks);
                int i = 0;
                while (i<chunks) {
                    System.out.println("Sending " + i + ". package");
                    out.write(Arrays.copyOfRange(bs, i * buffSize, i * buffSize + buffSize), 0, buffSize);
                    out.flush();
                    System.out.println(receive.read());
                    ++i;
                }
                out.write(Arrays.copyOfRange(bs, i*buffSize, i*buffSize+(bs.length - i*buffSize)), 0, (bs.length - i*buffSize));
                out.flush();
                System.out.println(receive.read());
                out.write("ok".getBytes(), 0, 2);
                out.flush();
            } catch (Exception e) {
                System.out.println("Exception occured while sending image " + e.getMessage());
            }

            System.out.println("Reading Image");
            int size = receive.readInt();

            System.out.println("Size: " + size);
            byte[] data = new byte[size];
            out.write("ok".getBytes(), 0, 2);
            out.flush();
            int chunks = size / 1024;
            System.out.println("chuncks " + chunks);
            int i = 0;
            while (i<chunks) {
                System.out.println("Receiving " + i + ". package");
                byte[] chunk = new byte[1024];
                receive.read(chunk);
                System.arraycopy(chunk, 0, data, i * 1024, 1024);
                out.write("ok".getBytes(), 0, 2);
                out.flush();
                ++i;
            }
            byte[] chunk = new byte[1024];
            receive.read(chunk);
            System.out.println(size - i * 1024);
            System.arraycopy(chunk, 0, data, i * 1024, size - i * 1024);
            Bitmap bmp = BitmapFactory.decodeByteArray(data , 0, size);
            if(bmp == null){
                System.err.println("Error receiving image");
            }
            return bmp;

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

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
//import java.nio.file.Files;
//import javax.imageio.ImageIO;

/**
 * Created by Reciproka on 2016.05.14..
 */
public class ImageToServerAsynctask extends AsyncTask<Void, Void, Bitmap> {

    Socket socket;
    File image;
    OutputStream outputStream;
    InputStream inputStream;
    static ByteArrayOutputStream imagetoByte;
    DataOutputStream dos;
    private static final String HOST = "192.168.43.99";
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);

            String serverMsg = bufferedReader.readLine();
            System.out.println("MSG: " + serverMsg);

            byte[] bs;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,480,620,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            bs= stream.toByteArray();

            System.out.println("Byte array length: " + bs.length);

            //printWriter.print(14);
            //printWriter.flush();
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(image));
            BufferedOutputStream out = null;
            try {
                out = new BufferedOutputStream(client.getOutputStream());
                final int buffSize = 4096;
                byte[] buffer = new byte[buffSize];
                int size;
                while ((size = input.read(buffer)) != -1) {
                    System.out.println("data: " + size);
                    out.write(buffer, 0, size);
                    out.flush();
                }
                System.out.println(client.isClosed());

            } catch (Exception e) {}
            System.out.println(client.isClosed());

            try {
                byte[] b = new byte[client.getReceiveBufferSize()];
                BufferedInputStream recieve = new BufferedInputStream(client.getInputStream());
                recieve.read(b);

                byte[] data;

                System.out.println("Reading Image");
                InputStream in = client.getInputStream();

                data = new byte[client.getReceiveBufferSize()];

                in.read(data, 0, client.getReceiveBufferSize());
                return BitmapFactory.decodeByteArray(data , 0, data .length);
           //     Image image = ImageIO.read(new ByteArrayInputStream(data));
             //   Bitmap bi = new Bitmap(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            //    File outputfile = new File("saved.jpg");
             //   ImageIO.write(bi, "jpg", outputfile);
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

package com.example.admin.oceanclean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class BackgroundWorker extends AsyncTask<String, Void, String>
{
    private Context context;
    private String type, username, admin;
    BackgroundWorker(Context ctx)
    {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params)
    {
        type = params[0];
        String login_url = "http://51.38.178.221/login.php";
        String report_url = "http://51.38.178.221/reportlitter.php";
        if (type.equals("login"))
        {
            try
            {
                username = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (type.equals("reportlitter"))
        {
            try
            {
                username = params[1];
                admin = params[2];
                String latitude = params[3];
                String longitude = params[4];
                String category = params[5];
                String commentNotes = params[6];
                URL url = new URL(report_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + "&"
                        + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8") + "&"
                        + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8") + "&"
                        + URLEncoder.encode("commentNotes", "UTF-8") + "=" + URLEncoder.encode(commentNotes, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (type.equals("register"))
        {
            try
            {
                String rusername = params[1];
                String email = params[2];
                String password = params[3];
                String confirmPassword = params[4];
                URL url = new URL("http://51.38.178.221/register.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("rusername", "UTF-8") + "=" + URLEncoder.encode(rusername, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("confirmPassword", "UTF-8") + "=" + URLEncoder.encode(confirmPassword, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (type.equals("changeemail")) {
            try
            {
                username = params[1];
                admin = params[2];
                String email = params[3];
                String newemail = params[4];
                URL url = new URL("http://51.38.178.221/changeemail.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("newemail", "UTF-8") + "=" + URLEncoder.encode(newemail, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if (type.equals("changepassword")) {
            try
            {
                username = params[1];
                admin = params[2];
                String password = params[3];
                String newpassword = params[4];
                URL url = new URL("http://51.38.178.221/changepassword.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("newpassword", "UTF-8") + "=" + URLEncoder.encode(newpassword, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if(type.equals("removelittermarkers")){
            try
            {
                username = params[1];
                admin = params[2];
                URL url = new URL("http://51.38.178.221/removelittermarkers.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else if(type.equals("removelitter")){
            try
            {
                username = params[1];
                admin = params[2];
                String urlremove = params[3];
                URL url = new URL("http://51.38.178.221/removelitter.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("urlremove", "UTF-8") + "=" + URLEncoder.encode(urlremove, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (type.equals("login")) {
            if (!result.equals("fail")) {
                Intent newintent = new Intent();
                newintent.putExtra("realUsername", username);
                newintent.putExtra("admin", result);
                newintent.setClass(context, MenuActivity.class);
                context.startActivity(newintent);
            } else {
                Toast.makeText(context, "There isn't an account with this username and password combination", Toast.LENGTH_LONG).show();
            }
        }else if (type.equals("register")){
            if (result.equals("id")){
                Toast.makeText(context, "This username already exists.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("email")){
                Toast.makeText(context, "There is already an account using this email.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("pw")){
                Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("format")) {
                Toast.makeText(context, "Invalid email format.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("done")){
                Intent newintent = new Intent();
                newintent.setClass(context, LoginActivity.class);
                context.startActivity(newintent);
                Toast.makeText(context, "Account successfully created.", Toast.LENGTH_SHORT).show();
            }
        }else if(type.equals("changeemail")){
            if(result.equals("email")){
                Toast.makeText(context, "Wrong email.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("format")){
                Toast.makeText(context, "Invalid email format.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("done")){
                Intent newintent = new Intent();
                newintent.putExtra("realUsername", username);
                newintent.putExtra("admin", admin);
                newintent.setClass(context, OptionsActivity.class);
                context.startActivity(newintent);
                Toast.makeText(context, "Email changed successfully", Toast.LENGTH_SHORT).show();
            }
        }else if(type.equals("changepassword")){
            if(result.equals("pw")){
                Toast.makeText(context, "Wrong password.", Toast.LENGTH_SHORT).show();
            }else if(result.equals("done")){
                Intent newintent = new Intent();
                newintent.putExtra("realUsername", username);
                newintent.putExtra("admin", admin);
                newintent.setClass(context, OptionsActivity.class);
                context.startActivity(newintent);
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();
            }
        }else if(type.equals("reportlitter")){
            if(result.equals("done")){
                Intent newintent = new Intent();
                newintent.putExtra("realUsername", username);
                newintent.putExtra("admin", admin);
                newintent.setClass(context, MenuActivity.class);
                context.startActivity(newintent);
                Toast.makeText(context, "Report sent successfully!", Toast.LENGTH_SHORT).show();
            }else if(result.equals("fail")){
                Toast.makeText(context, "Report not sent, something failed!", Toast.LENGTH_SHORT).show();
            }
        }else if(type.equals("removelittermarkers")){
            int numero = 0;
            int indice = 1;
            Intent removeIntent = new Intent();
            removeIntent.putExtra("realUsername", username);
            removeIntent.putExtra("admin", admin);
            String[] separated = result.split("<br/>");
            removeIntent.putExtra("amount", separated[0]);
            for(int i=1; i<=(Integer.parseInt(separated[0])*5); i++){
                if(numero == 0){
                    removeIntent.putExtra("latitude"+indice, separated[i]);
                    numero = 1;
                }else if(numero == 1){
                    removeIntent.putExtra("longitude"+indice, separated[i]);
                    numero = 2;
                }else if(numero == 2){
                    removeIntent.putExtra("category"+indice, separated[i]);
                    numero = 3;
                }else if(numero == 3){
                    removeIntent.putExtra("description"+indice, separated[i]);
                    numero = 4;
                }else if(numero == 4){
                    removeIntent.putExtra("photo"+indice, separated[i]);
                    numero = 0;
                    indice++;
                }
            }
            removeIntent.setClass(context, RemoveActivity.class);
            context.startActivity(removeIntent);
        }else if(type.equals("removelitter")){
            if(result.equals("done")){
                Intent newintent = new Intent();
                newintent.putExtra("realUsername", username);
                newintent.putExtra("admin", admin);
                newintent.setClass(context, MenuActivity.class);
                context.startActivity(newintent);
                Toast.makeText(context, "Litter removed successfully!", Toast.LENGTH_SHORT).show();
            }else if(result.equals("fail")){
                Toast.makeText(context, "Litter not removed, something failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }
}

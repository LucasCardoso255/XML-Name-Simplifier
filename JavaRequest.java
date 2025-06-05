import java.net.URL;
import java.net.HttpURLConnection;

public class JavaRequest{
	public static void main (String []args){
		
		try{
			URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
			HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
			conexao.setRequestMethod("GET");

			int respostaNum = conexao.getResponseCode();
			System.out.print("Código de resposta: " + respostaNum);
		}
		catch(Exception e){
		System.out.print(e);
		}	
	}
}
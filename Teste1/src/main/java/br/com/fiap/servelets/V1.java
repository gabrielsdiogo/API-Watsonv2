package br.com.fiap.servelets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.MessageResponse;

/**
 * Servlet implementation class V1
 */
@WebServlet("/V1")
public class V1 extends HttpServlet {
	private Context context = null;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public V1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String msg = req.getParameter("question");
		System.out.println(msg);

		MessageResponse response = this.assistantAPICall(msg);
		
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(response.getOutput().getText()));
	}
	
	
	private MessageResponse assistantAPICall(String msg) {

		// Configuração de autenticação do serviço *********************************************
		IamOptions options = new IamOptions.Builder()
				//Colocar a sua APIKEY
				.apiKey("-21eHSVO02PQiH5PI2q99moKQinxkSHBollVt0vfBnBJ")
				.build();
		
		// Criando o objeto do serviço desejado ************************************************
		
		Assistant service = new Assistant("2018-02-16", options);
		      //Colocar a sua WORKSPACEID
		String workspaceId = "4785ae2a-4a9a-445f-beec-658c898843c6";
		
		// Preparando a mensagem de envio *****************************************************
		MessageInput input = new MessageInput();
		input.setText(msg);
		
		// Configurando os parametros para o Watson *******************************************
		MessageOptions messageOptions = new MessageOptions.Builder()
				.workspaceId(workspaceId)
				.input(input)
				.context(this.context)
				.build();
		
		// Conectando com o Assistant e recebendo a resposta dele ******************************
		MessageResponse response  = service.message(messageOptions)
				.execute()
				.getResult();
		
		this.context = response.getContext();

		return response;
	}

}

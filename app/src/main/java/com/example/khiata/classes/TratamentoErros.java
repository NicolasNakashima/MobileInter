package com.example.khiata.classes;

import android.util.Log;

import org.json.JSONObject;

import retrofit2.Response;

public class TratamentoErros {

    //Tratamento dos erros do Firebase
    public String tratandoErroFirebase(Exception e){
        String errorMessage = "Ocorreu um erro. Tente novamente mais tarde."; // Mensagem padrão

        if (e != null && e.getMessage() != null) {
            String firebaseMessage = e.getMessage();

            if (firebaseMessage.contains("There is no user record")) {
                errorMessage = "Usuário não encontrado. Verifique o e-mail informado.";
            } else if (firebaseMessage.contains("The password is invalid")) {
                errorMessage = "Senha incorreta. Por favor, tente novamente.";
            } else if (firebaseMessage.contains("The email address is already in use")) {
                errorMessage = "Este e-mail já está em uso. Tente um diferente.";
            } else if (firebaseMessage.contains("We have blocked all requests from this device")) {
                errorMessage = "As solicitações deste dispositivo foram bloqueadas temporariamente. Tente mais tarde.";
            } else if (firebaseMessage.contains("A network error")) {
                errorMessage = "Erro de rede. Verifique sua conexão com a internet.";
            }
        }
        return errorMessage;
    }

    //Tratamento dos erros da API
    public String tratandoErroThrowable(Throwable t){
        String errorMessage = "Ocorreu um erro. Tente novamente mais tarde.";

        if (t != null && t.getMessage() != null) {
            String apiErrorMessage = t.getMessage();

            if (apiErrorMessage.contains("Unable to resolve host")) {
                errorMessage = "Sem conexão com a internet. Verifique sua conexão.";
            } else if (apiErrorMessage.contains("timeout")) {
                errorMessage = "A conexão demorou muito para responder. Tente novamente mais tarde.";
            } else if (apiErrorMessage.contains("500")) {
                errorMessage = "Erro no servidor. Tente novamente mais tarde.";
            } else if (apiErrorMessage.contains("404")) {
                errorMessage = "Recurso não encontrado. Verifique as informações enviadas.";
            } else if (apiErrorMessage.contains("401")) {
                errorMessage = "Não autorizado. Verifique suas credenciais.";
            } // Adicione outros mapeamentos conforme necessário
        }

        return errorMessage;
    }

    //Tratamento dos erros da API
    public String tratandoErroApi(Response<?> response){
        String errorMessage = "Ocorreu um erro. Tente novamente mais tarde.";

        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                // Suponha que o erro seja um JSON com um campo 'message'
                JSONObject jsonObject = new JSONObject(errorBody);
                errorMessage = jsonObject.optString("message", errorMessage); // Pega a mensagem se existir
            }
        } catch (Exception e) {
            Log.e("Error Parsing", "Erro ao parsear o errorBody", e);
        }
        return errorMessage;
    }
}

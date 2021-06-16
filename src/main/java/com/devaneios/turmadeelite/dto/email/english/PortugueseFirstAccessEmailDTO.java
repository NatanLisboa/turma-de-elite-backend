package com.devaneios.turmadeelite.dto.email.english;

import com.devaneios.turmadeelite.dto.email.FirstAccessEmailDTO;
import lombok.Builder;

public class PortugueseFirstAccessEmailDTO extends FirstAccessEmailDTO {
    private static final String subject = "[Turma de Elite] Primeiro Acesso";

    public PortugueseFirstAccessEmailDTO(String to, String name, String link) {
        super(to, subject, makeText(name,link));
    }

    private static String makeText(String name, String link){
        return "Olá " + name + ", seja bem-vindo! Você foi cadastrado(a) na plataforma Turma de Elite. Para acessar seu ambiente, realize seu primeiro acesso com os seguintes passos:" +
                "Passo 1: acesse o link " + link +
                "Passo 2: defina sua senha através dos campos \"Senha\" e \"Confirmar senha\"." +
                "Passo 3: clique em \"Realizar primeiro acesso\"" +
                "Passo 4: realize o login inserindo o e-mail e senha inseridos no primeiro acesso" +

                "O primeiro acesso na aplicaçao só poderá ser realizado uma única vez, em caso de esquecimento de senha, você poderá recuperá-la utilizando o botão \"Esqueci a senha\" localizado na tela de login." +
                " O link do primeiro acesso é pessoal e instransferível, e expirará assim que o primeiro acesso for confirmado. " +

                "Se você não estava esperando este e-mail, por favor deconsidere-o" +

                "Atenciosamente, equipe administrativa da plataforma Turma de Elite";
    }
}

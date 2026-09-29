package br.com.nutriexpress.demo.exception;

public class PratoNaoEncontradoException extends RuntimeException {

    public PratoNaoEncontradoException(Long id) {
        super("Prato não encontrado com o ID: " + id);
    }

    public PratoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

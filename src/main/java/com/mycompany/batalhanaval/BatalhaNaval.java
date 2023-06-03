package com.mycompany.batalhanaval;
import java.util.Random;
import java.util.Scanner;
public class BatalhaNaval 
{
    private static final int TAMANHO_MAPA = 10;
    private static final int NUMERO_JOGADORES = 2;
    private static final int[] TAMANHO_BARCOS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private static final String[] NOMES_BARCOS = {"Porta-aviões", "Encouraçado", "Cruzador", "Submarino", "Destroyer",
            "Navio de Guerra", "Barco 1", "Barco 2", "Barco 3", "Barco 4"};
    private static final char VAZIO = '?';
    private static final char ATINGIDO = 'B';
    private static final char ERRO = 'O';
    private final char[][][] mapas;
    private final int[] pontuacoes;
    public BatalhaNaval() 
    {
        mapas = new char[NUMERO_JOGADORES][TAMANHO_MAPA][TAMANHO_MAPA];
        pontuacoes = new int[NUMERO_JOGADORES];
        inicializarMapas();
    }
    public void jogar() 
    {
        System.out.println("Bem-vindo à Batalha Naval!");

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < NUMERO_JOGADORES; i++) {
            System.out.println("Jogador " + (i + 1) + ", é a sua vez de posicionar os barcos.");

            System.out.print("Deseja posicionar os barcos manualmente? (s/n): ");
            String resposta = scanner.next();
            
            boolean posicionarManualmente = resposta.equalsIgnoreCase("s");
            posicionarBarcos(i, scanner, posicionarManualmente);
        }

        int jogadorAtual = 0;
        int jogadorOponente = 1;

        boolean contraMaquina = false;
        System.out.print("Deseja jogar contra a máquina? (s/n): ");
        String respostaMaquina = scanner.next();
        
        if (respostaMaquina.equalsIgnoreCase("s"))
        {
            contraMaquina = true;
            System.out.println("Você está jogando contra a máquina.");
        }

        while (true) 
        {
             System.out.println("\nJogador " + (jogadorAtual + 1) + ", é a sua vez de atirar.");

            if (contraMaquina && jogadorAtual == 1) {
                realizarAtaqueMaquina(jogadorAtual, jogadorOponente);
            } else {
                exibirMapa(jogadorAtual);
                realizarAtaque(jogadorAtual, jogadorOponente, scanner);
            }

            if (verificarFimDeJogo(jogadorOponente)) {
                System.out.println("\nParabéns! Jogador " + (jogadorAtual + 1) + " venceu o jogo!");
                break;
            }
            
            System.out.println("\nPressione Enter para continuar para o próximo jogador...");
            scanner.nextLine(); // Aguarda a entrada do jogador para prosseguir

            int temp = jogadorAtual;
            jogadorAtual = jogadorOponente;
            jogadorOponente = temp;
        }
    }
    private void inicializarMapas()
    {
        for (int i = 0; i < NUMERO_JOGADORES; i++) {
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                for (int k = 0; k < TAMANHO_MAPA; k++) {
                    mapas[i][j][k] = VAZIO;
                }
            }
        }
    }
  private void exibirMapa(int jogador)
  {
    System.out.println("    A B C D E F G H I J");
    
    for (int i = 0; i < TAMANHO_MAPA; i++) 
    {
        System.out.print(i + "   ");
        
        for (int j = 0; j < TAMANHO_MAPA; j++)
        {
            char c = mapas[jogador][i][j];
            
            if (c == VAZIO || c == ATINGIDO || c == ERRO)
            {
                System.out.print(c + " ");
            } 
            else 
            {
                System.out.print(VAZIO + " ");
            }
        }
        System.out.println();
    }
}
    private void posicionarBarcos(int jogador, Scanner scanner, boolean posicionarManualmente) 
    {
        if (posicionarManualmente) 
        {
            posicionarBarcosManualmente(jogador, scanner);
        } 
        else
        {
            posicionarBarcosAutomaticamente(jogador);
        }
    }
    private void posicionarBarcosAutomaticamente(int jogador)
    {
        Random random = new Random();
        
        for (int i = 0; i < TAMANHO_BARCOS.length; i++)
        {
            boolean posicaoValida = false;
            
            while (!posicaoValida)
            {
                int linha = random.nextInt(TAMANHO_MAPA);
                
                int coluna = random.nextInt(TAMANHO_MAPA);
                
                String orientacao = random.nextBoolean() ? "H" : "V";

                if (verificarPosicaoDisponivel(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao)) 
                {
                    posicionarBarco(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao);
                    posicaoValida = true;
                }
            }
        }
    }
   private void posicionarBarcosManualmente(int jogador, Scanner scanner)
{
    for (int i = 0; i < TAMANHO_BARCOS.length; i++)
    {
        exibirMapa(jogador);
        System.out.println("Posicione o " + NOMES_BARCOS[i] + " (" + TAMANHO_BARCOS[i] + " espaços):");

        boolean posicaoValida = false;
        while (!posicaoValida) 
        {
            System.out.print("Digite a linha (0-9): ");
            int linha = scanner.nextInt();
            
            System.out.print("Digite a coluna (A-J): ");
            String colunaStr = scanner.next();
            
            int coluna = colunaStr.charAt(0) - 'A';

            System.out.print("Digite a orientação (H para horizontal, V para vertical): ");
            String orientacao = scanner.next();

            if (verificarPosicaoDisponivel(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao))
            {
                posicionarBarco(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao);
                posicaoValida = true;
            } 
            else
            {
                System.out.println("Posição inválida. Tente novamente.");
            }
        }
    }
}
    private boolean verificarPosicaoDisponivel(int jogador, int linha, int coluna, int tamanho, String orientacao) 
    {
        if (orientacao.equalsIgnoreCase("H"))
        {
            if (coluna + tamanho > TAMANHO_MAPA) 
            {
                return false;
            }
            for (int i = coluna; i < coluna + tamanho; i++) 
            {
                if (mapas[jogador][linha][i] != VAZIO)
                {
                    return false;
                }
            }
        } 
        else if (orientacao.equalsIgnoreCase("V"))
        {
            if (linha + tamanho > TAMANHO_MAPA) 
            {
                return false;
            }
            for (int i = linha; i < linha + tamanho; i++) 
            {
                if (mapas[jogador][i][coluna] != VAZIO)
                {
                    return false;
                }
            }
        } 
        else 
        {
            return false;
        }
        return true;
    }
    private void posicionarBarco(int jogador, int linha, int coluna, int tamanho, String orientacao)
    {
        if (orientacao.equalsIgnoreCase("H"))
        {
            for (int i = coluna; i < coluna + tamanho; i++)
            {
                mapas[jogador][linha][i] = 'B';
            }
        } 
        else if (orientacao.equalsIgnoreCase("V"))
        {
            for (int i = linha; i < linha + tamanho; i++)
            {
                mapas[jogador][i][coluna] = 'B';
            }
        }
    }
    private void realizarAtaque(int jogador, int oponente, Scanner scanner)
    {
        boolean jogadaValida = false;
        while (!jogadaValida) 
        {
            System.out.println("\nMapa do Jogador " + (jogador + 1) + ":");
            exibirMapa(jogador);
            
            System.out.print("\nDigite a linha (0-9) para atacar: ");
            int linha = scanner.nextInt();
            
            System.out.print("Digite a coluna (A-J) para atacar: ");
            String colunaStr = scanner.next();
            
            int coluna = colunaStr.charAt(0) - 'A';
            if (linha < 0 || linha >= TAMANHO_MAPA || coluna < 0 || coluna >= TAMANHO_MAPA)
            {
                System.out.println("Jogada inválida. Tente novamente.");
                continue;
            }

            switch (mapas[oponente][linha][coluna]) {
                case VAZIO -> {
                    System.out.println("Água! Tente novamente na próxima rodada.");
                    mapas[oponente][linha][coluna] = ERRO;
                    jogadaValida = true;
                }
                case ATINGIDO, ERRO -> System.out.println("Você já atacou essa posição. Tente novamente.");
                default -> {
                    System.out.println("Acertou um barco!");
                    mapas[oponente][linha][coluna] = ATINGIDO;
                    pontuacoes[jogador]++;
                    jogadaValida = true;
                }
            }
        }
    }
    private void realizarAtaqueMaquina(int jogador, int oponente)
    {
        Random random = new Random();
        boolean jogadaValida = false;

        while (!jogadaValida)
        {
            int linha = random.nextInt(TAMANHO_MAPA);
            
            int coluna = random.nextInt(TAMANHO_MAPA);

            switch (mapas[oponente][linha][coluna]) 
            {
                case VAZIO -> 
                {
                    System.out.println("\nA máquina atacou a posição " + (char)('A' + coluna) + linha + ": Água!");
                    mapas[oponente][linha][coluna] = ERRO;
                    jogadaValida = true;
                    break;
                }
                case ATINGIDO, ERRO -> 
                {
                    continue;
                }
                default -> 
                {
                    System.out.println("\nA máquina atacou a posição " + (char)('A' + coluna) + linha + ": Acertou um barco!");
                    mapas[oponente][linha][coluna] = ATINGIDO;
                    pontuacoes[jogador]++;
                    jogadaValida = true;
                }
            }
        }
    }
    private boolean verificarFimDeJogo(int jogador) 
    {
        for (int i = 0; i < TAMANHO_MAPA; i++)
        {
            for (int j = 0; j < TAMANHO_MAPA; j++) 
            {
                if (mapas[jogador][i][j] != VAZIO && mapas[jogador][i][j] != ATINGIDO)
                {
                    return false;
                }
            }
        }
        return true;
    }
    public static void main(String[] args)
    {
        BatalhaNaval jogo = new BatalhaNaval();
        jogo.jogar();
    }
}
package com.mycompany.batalhanaval;
import java.util.Random;
import java.util.Scanner;
public class BatalhaNaval
{
    private static final int TAMANHO_MAPA = 10; // Tamanho do mapa
    private static final int TOTAL_BARCOS = 10;  // Número total de barcos
    private static final int NUMERO_JOGADORES = 2;    // Número de jogadores
    private static final int[] TAMANHO_BARCOS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};  // Tamanhos dos barcos
    private static final String[] NOMES_BARCOS = {"Porta-aviões", "Encouraçado", "Cruzador", 
    "Submarino", "Destroyer", "Navio de Guerra", "Barco 1", "Barco 2", "Barco 3", "Barco 4"}; // Nomes dos barcos
    private static final char VAZIO = '~'; // Caractere para representar posição vazia no mapa
    private static final char ATINGIDO = 'X';// Caractere para representar posição atingida no mapa
    private static final char ERRO = 'O'; // Caractere para representar posição de erro no mapa
    private static final char BARCO = 'B';// Caractere para representar barco no mapa
    private final char[][][] mapas; // Mapas dos jogadores
    private final int[] pontuacoes; // Pontuações dos jogadores

    public BatalhaNaval() {
        mapas = new char[NUMERO_JOGADORES][TAMANHO_MAPA][TAMANHO_MAPA]; // Inicializa os mapas dos jogadores
        pontuacoes = new int[NUMERO_JOGADORES];  // Inicializa as pontuações dos jogadores
        inicializarMapas();
    }

   public void jogar() {
    System.out.println("Bem-vindo à Batalha Naval!");
    Scanner scanner = new Scanner(System.in); // Cria um scanner para receber entradas do usuário
    
    // Loop para cada jogador posicionar seus barcos
    for (int i = 0; i < NUMERO_JOGADORES; i++)
     {
        System.out.println("Jogador " + (i + 1) + ", é a sua vez de posicionar os barcos.");

        // Variável para armazenar a resposta do jogador sobre posicionar manualmente
        boolean posicionarManualmente = false;
        
        //Verifica a resposta do jogador sobre posicionar manualmente
        while (true) 
        {
            System.out.print("Deseja posicionar os barcos manualmente? (s/n): ");
            String resposta = scanner.next();

            // Verifica se o jogador deseja posicionar manualmente
            if (resposta.equalsIgnoreCase("s")) 
            {
                posicionarManualmente = true;
                break;
            } 
            else if (resposta.equalsIgnoreCase("n"))
             {
                break;
            } 
            else 
            {
                System.out.println("Resposta inválida. Digite 's' para sim ou 'n' para não.");
            }
        }
        // Chama o método para posicionar os barcos do jogador atual
        posicionarBarcos(i, scanner, posicionarManualmente);
    }
    // Variáveis para controlar o jogador atual e oponente
    int jogadorAtual = 0;
    int jogadorOponente = 1;
    boolean contraMaquina = false;
    // Loop para verificar se o jogador deseja jogar contra a máquina
    while (true) 
    {
        System.out.print("Deseja jogar contra a máquina? (s/n): ");
        String respostaMaquina = scanner.next();

        // Verifica a resposta do jogador sobre jogar contra a máquina
        if (respostaMaquina.equalsIgnoreCase("s")) 
        {
            contraMaquina = true;
            System.out.println("Você está jogando contra a máquina.");
            break;
        } 
        else if (respostaMaquina.equalsIgnoreCase("n"))
         {
            break;
        } 
        else 
        {
            System.out.println("Resposta inválida. Digite 's' para sim ou 'n' para não.");
        }
    }
    
    // Loop principal do jogo
    while (true)
     {
        // Exibe mensagem indicando a vez do jogador atual para atirar
        System.out.println("\nJogador " + (jogadorAtual + 1) + ", é a sua vez de atirar.");

        // Verifica se é a vez do jogador atual atacar ou se a máquina irá atacar
        if (contraMaquina && jogadorAtual == 1)
        {
            realizarAtaqueMaquina(jogadorAtual, jogadorOponente);
        } 
        else 
        {
            realizarAtaque(jogadorAtual, jogadorOponente, scanner);
        }
        
        // Verifica se o jogo chegou ao fim
        if (verificarFimDeJogo(jogadorOponente))
         {
            System.out.println("\nParabéns! Jogador " + (jogadorAtual + 1) + " venceu o jogo!");
            break;
        }
        System.out.println("\nPressione Enter para continuar para o próximo jogador...");
        scanner.nextLine(); 
   
        // Troca os jogadores atual e oponente
        int temp = jogadorAtual;
        jogadorAtual = jogadorOponente;
        jogadorOponente = temp;
    }
}

    private void inicializarMapas() {
    // Loop para percorrer os jogadores
    for (int i = 0; i < NUMERO_JOGADORES; i++)
     {
        // Loop para percorrer as linhas do mapa
        for (int j = 0; j < TAMANHO_MAPA; j++) 
        {
            // Loop para percorrer as colunas do mapa
            for (int k = 0; k < TAMANHO_MAPA; k++) 
            {
                // Inicializa cada posição do mapa do jogador com o valor VAZIO
                mapas[i][j][k] = VAZIO;
            }
        }
    }
}

private void exibirMapa(int jogador, boolean exibirBarcos) {
     System.out.println(" | A  | B  | C  |  D |  E |  F | G  | H  | I  |  J |");
    System.out.println("+--------------------------------------------------+");
    
    // Loop para percorrer as linhas do mapa
    for (int i = 0; i < TAMANHO_MAPA; i++) 
    {
        // Imprime o número da linha
        System.out.print(i);
        
        // Loop para percorrer as colunas do mapa
        for (int j = 0; j < TAMANHO_MAPA; j++) 
        {
            // Obtém o caractere da posição do mapa do jogador
            char c = mapas[jogador][i][j];
            System.out.print("| ");
            
            // Verifica o valor do caractere e imprime o caractere correspondente
            if (c == 'X' || c == ATINGIDO || c == VAZIO || c == ERRO) 
            {
                System.out.print(c + "  "); // Caractere já revelado
            } 
            else if (exibirBarcos && c == BARCO) 
            {
                System.out.print(c + "  "); // Caractere de barco quando exibirBarcos é verdadeiro
            }
            else
            {
                System.out.print("~  "); // Caractere para representar uma posição não revelada
            }
        }
        // Imprime a barra vertical de fechamento da linha
        System.out.println("|");
    }
    
    // Imprime a linha inferior do mapa
   System.out.println("+--------------------------------------------------+");
}

private void exibirMapaAtaques(int oponente) {
    System.out.println(" | A  | B  | C  |  D |  E |  F | G  | H  | I  |  J |");
    System.out.println("+--------------------------------------------------+");
    // Loop para percorrer as linhas do mapa
    for (int i = 0; i < TAMANHO_MAPA; i++) 
    {
           System.out.print(i);
        // Loop para percorrer as colunas do mapa
        for (int j = 0; j < TAMANHO_MAPA; j++) 
        {
       
            // Obtém o caractere da posição do mapa do oponente
            char c = mapas[oponente][i][j];
              System.out.print("| ");
            // Verifica o valor do caractere e imprime o caractere correspondente
            if (c == 'X' || c == ATINGIDO || c == VAZIO || c == ERRO) 
            {
                System.out.print(c + "  "); // Caractere já revelado
            } 
            else
            {
                System.out.print("~  "); // Caractere para representar uma posição não revelada
            }
        }
        System.out.println("|");
    }
    System.out.println("+--------------------------------------------------+");
}

private void posicionarBarcos(int jogador, Scanner scanner, boolean posicionarManualmente) {
    // Verifica se a opção de posicionar manualmente os barcos foi escolhida
    if (posicionarManualmente) 
    {
        // Chama o método para posicionar os barcos manualmente
        posicionarBarcosManualmente(jogador, scanner);
    } 
    else 
    {
        // Chama o método para posicionar os barcos automaticamente
        posicionarBarcosAutomaticamente(jogador);
    }
}

  private void posicionarBarcosAutomaticamente(int jogador) {
    Random random = new Random();
   
    // Itera sobre os tamanhos dos barcos
    for (int i = 0; i < TAMANHO_BARCOS.length; i++) {
        boolean posicaoValida = false;
       
        // Enquanto a posição não for válida
        while (!posicaoValida) 
        {
            // Gera coordenadas aleatórias
            int linha = random.nextInt(TAMANHO_MAPA);
            int coluna = random.nextInt(TAMANHO_MAPA);
           
            // Gera uma orientação aleatória (horizontal ou vertical)
            String orientacao = random.nextBoolean() ? "H" : "V";

            // Verifica se a posição e orientação geradas são válidas
            if (verificarPosicaoDisponivel(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao)) 
            {
                // Posiciona o barco na posição gerada
                posicionarBarco(jogador, linha, coluna, TAMANHO_BARCOS[i], orientacao);
                posicaoValida = true;
            }
        }
    }
}

 private void posicionarBarcosManualmente(int jogador, Scanner scanner) {
    int linha=0;
    for (int i = 0; i < TAMANHO_BARCOS.length; i++) 
    {
        exibirMapa(jogador, true);
        // Mostra o mapa atualizado antes de cada posicionamento de barco
        System.out.println("Posicione o " + NOMES_BARCOS[i] + " (" + TAMANHO_BARCOS[i] + " espaços):");

        boolean posicaoValida = false;
        
        while (!posicaoValida)
         {
            System.out.print("Digite a linha (0-9): ");
            String linhaStr = scanner.next();

         if (linhaStr.matches("[0-9]"))
            {
        linha = Integer.parseInt(linhaStr);
        if (linha >= 0 && linha <= 9) 
        {
            break;
        } 
        else 
        {
            System.out.println("Entrada inválida. Digite novamente a linha (0-9): ");
        }
        } 
        else 
        {
            System.out.println("Entrada inválida. Digite novamente a linha (0-9): ");
         }
}
            System.out.print("Digite a coluna (A-J): ");
            String colunaStr;
            while (true) 
            {
            colunaStr = scanner.next().toUpperCase();
            if (colunaStr.length() == 1)
            {
            char colunaChar = colunaStr.charAt(0);
            if (colunaChar >= 'A' && colunaChar <= 'J')
            {
            break;
            }
            }
            System.out.println("Entrada inválida. Digite novamente a coluna (A-J): ");
            }
            int coluna = colunaStr.charAt(0) - 'A';

         System.out.print("Digite a orientação (H para horizontal, V para vertical): ");
         String orientacao;

            if (TAMANHO_BARCOS[i] == 1)
             {
            orientacao = "H"; // Define a orientação como horizontal quando o tamanho do barco for 1
            System.out.println("Orientação (padrão)");
            } 
            else
         {
    while (true) 
    {
        orientacao = scanner.next();
        if (orientacao.equalsIgnoreCase("H") || orientacao.equalsIgnoreCase("V")) 
        {
            break;
        }
        System.out.println("Entrada inválida. Digite novamente a orientação (H para horizontal, V para vertical): ");
    }
}
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

   private boolean verificarPosicaoDisponivel(int jogador, int linha, int coluna, int tamanho, String orientacao) {
    
    // Verifica se a orientação é horizontal
    if (orientacao.equalsIgnoreCase("H")) 
    {
        // Verifica se o barco ultrapassa os limites do mapa na horizontal
        if (coluna + tamanho > TAMANHO_MAPA) 
        {
            return false; // Posição inválida, o barco ultrapassa os limites do mapa
        }
        // Verifica se há algum obstáculo (não vazio) na posição do mapa onde o barco será posicionado
        for (int i = coluna; i < coluna + tamanho; i++) 
        {
            if (mapas[jogador][linha][i] != VAZIO) 
            {
                return false; // Posição inválida, há um obstáculo no caminho
            }
        }
    }

    // Verifica se a orientação é vertical
    else if (orientacao.equalsIgnoreCase("V")) 
    {
        // Verifica se o barco ultrapassa os limites do mapa na vertical
        if (linha + tamanho > TAMANHO_MAPA)
         {
            return false; // Posição inválida, o barco ultrapassa os limites do mapa
        }
        // Verifica se há algum obstáculo (não vazio) na posição do mapa onde o barco será posicionado
        for (int i = linha; i < linha + tamanho; i++) 
        {
            if (mapas[jogador][i][coluna] != VAZIO) 
            {
                return false; // Posição inválida, há um obstáculo no caminho
            }
        }
    }
    else 
    {
        return false; // Orientação inválida
    }
    return true; // A posição está disponível para posicionar o barco
}

    private void posicionarBarco(int jogador, int linha, int coluna, int tamanho, String orientacao) {
    // Verifica se a orientação é horizontal
    if (orientacao.equalsIgnoreCase("H")) 
    {
        // Posiciona o barco nas posições correspondentes no mapa
        for (int i = coluna; i < coluna + tamanho; i++) 
        {
            mapas[jogador][linha][i] = 'B';
        }
    }
    // Verifica se a orientação é vertical
    else if (orientacao.equalsIgnoreCase("V")) 
    {
        // Posiciona o barco nas posições correspondentes no mapa
        for (int i = linha; i < linha + tamanho; i++) 
        {
            mapas[jogador][i][coluna] = 'B';
        }
    }
}

    private void realizarAtaque(int jogador, int oponente, Scanner scanner) {
    boolean jogadaValida = false;
    System.out.println("\nSeu Mapa");
    exibirMapa(jogador, false);

    System.out.println("\nMapa do Adversário ");
    exibirMapaAtaques(oponente);
    
    System.out.print("\nPressione Enter para continuar...");
    scanner.nextLine(); // Aguarda a entrada do usuário
    
    while (!jogadaValida)
    {
        System.out.print("\nDigite a linha (0-9) para atacar: ");
        int linha;
        while (true)
        {
            try {
                linha = Integer.parseInt(scanner.next());
                if (linha >= 0 && linha <= 9) 
                {
                    break;
                }
            } 
            catch (NumberFormatException e)
            {
                System.out.print("Entrada inválida. Digite novamente a linha (0-9): ");
            }
        }
        System.out.print("Digite a coluna (A-J): ");
        String colunaStr;

        while (true)
        {
            colunaStr = scanner.next().toUpperCase();
            if (colunaStr.length() == 1) 
            {
                char colunaChar = colunaStr.charAt(0);

            if (colunaChar >= 'A' && colunaChar <= 'J')
            {
            break;
            }
        }
    System.out.println("Entrada inválida. Digite novamente a coluna (A-J): ");
        }

        int coluna = colunaStr.charAt(0) - 'A';

        if (linha < 0 || linha >= TAMANHO_MAPA || coluna < 0 || coluna >= TAMANHO_MAPA)
        {
            System.out.println("Jogada inválida. Tente novamente.");
            continue;
        }

       switch (mapas[oponente][linha][coluna]) 
{
    case VAZIO:
        System.out.println("Água! Tente novamente na próxima rodada.");
        mapas[oponente][linha][coluna] = ERRO;
        jogadaValida = true;
        break;

    case ATINGIDO:
    case ERRO:
        System.out.println("Você já atacou essa posição. Tente novamente.");
        break;

    default:
        System.out.println("Acertou um barco!");
        mapas[oponente][linha][coluna] = ATINGIDO;
        pontuacoes[jogador]++;
        
        if (pontuacoes[jogador] == TOTAL_BARCOS)
        {
            System.out.println("Parabéns! Você afundou todos os barcos do adversário!");
        }
        else if (pontuacoes[jogador] > 0)
        {
            System.out.println("Parabéns! Você acertou uma parte de um barco do adversário!");
        }
        else if (pontuacoes[jogador] == 1)
        {
            System.out.println("Parabéns! Você afundou um barco!");
        }
        
        jogadaValida = true;
        break;
}

        if (verificarFimDeJogo(oponente)) 
        {
            System.out.println("Todos os barcos do adversário foram afundados! Você venceu o jogo!");
            jogadaValida = true;
        }
     
    }
}

private void realizarAtaqueMaquina(int jogador, int oponente) {
    Random random = new Random();
    boolean jogadaValida = false;

    while (!jogadaValida) 
    {
        int linha = random.nextInt(TAMANHO_MAPA);
        int coluna = random.nextInt(TAMANHO_MAPA);
        switch (mapas[oponente][linha][coluna])
         {
            case VAZIO:
                System.out.println("\nA máquina atacou a posição " + (char) ('A' + coluna) + linha + ": Água!");
                mapas[oponente][linha][coluna] = ERRO;
                jogadaValida = true;
                break;

            default:
                System.out.println("\nA máquina atacou a posição " + (char) ('A' + coluna) + linha + ": Acertou um barco");
                mapas[oponente][linha][coluna] = ATINGIDO;
                pontuacoes[jogador]++;

                if (pontuacoes[jogador] == TOTAL_BARCOS) 
                {
                    System.out.println("A máquina afundou todos os seus barcos!");
                }
                 else if (pontuacoes[jogador] == 1) 
                 {
                    System.out.println("A máquina afundou um dos seus barcos!");
                }
        }

        if (verificarFimDeJogo(oponente)){
            System.out.println("A máquina afundou todos os seus barcos! Você perdeu o jogo!");
            jogadaValida = true;
        }
    }
}
  private boolean verificarFimDeJogo(int jogador) {
    for (int i = 0; i < TAMANHO_MAPA; i++)
     {
        for (int j = 0; j < TAMANHO_MAPA; j++) 
        {
            // Verifica se a posição do mapa não é vazia e não foi atingida
            if (mapas[jogador][i][j] != VAZIO && mapas[jogador][i][j] != ATINGIDO) 
            {
                // Se encontrar uma posição que não foi vazia nem atingida, o jogo não acabou
                return false;
            }
        }
    }
    // Se todas as posições do mapa foram vazias ou atingidas, o jogo chegou ao fim
    return true;
}
    public static void main(String[] args){
        BatalhaNaval jogo = new BatalhaNaval();
        jogo.jogar();
    }
}

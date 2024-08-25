package Atividade.Util;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    public static class MenuGrafo {

        private static Grafo<String> grafo = new Grafo<>();
        private static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) {
            carregarGrafo(); // Carrega o grafo do arquivo no início
            int opcao;
            do {
                exibirMenu();
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (opcao) {
                    case 1:
                        adicionarVertice();
                        break;
                    case 2:
                        removerVertice();
                        break;
                    case 3:
                        adicionarAresta();
                        break;
                    case 4:
                        removerAresta();
                        break;
                    case 5:
                        buscaEmLargura();
                        break;
                    case 6:
                        buscaEmProfundidade();
                        break;
                    case 7:
                        imprimirTemposDFS();
                        break;
                    case 8:
                        verificarBipartido();
                        break;
                    case 9:
                        encontrarVR();
                        break;
                    case 10:
                        executarPrim();
                        break;
                    case 11:
                        executarKruskal();
                        break;
                    case 12:
                        executarBoruvka();
                        break;
                    case 13:
                        gerarCicloMinimo();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }

            } while (opcao != 0);
        }

        private static void exibirMenu() {
            System.out.println("\n--- Menu de Operações no Grafo ---");
            System.out.println("1. Adicionar Vértice");
            System.out.println("2. Remover Vértice");
            System.out.println("3. Adicionar Aresta");
            System.out.println("4. Remover Aresta");
            System.out.println("5. Executar Busca em Largura (BFS)");
            System.out.println("6. Executar Busca em Profundidade (DFS)");
            System.out.println("7. Imprimir Tempos de Chegada e Partida da DFS");
            System.out.println("8. Verificar se o Grafo é Bipartido");
            System.out.println("9. Encontrar Vértice Raiz (VR)");
            System.out.println("10. Gerar MST com Prim");
            System.out.println("11. Gerar MST com Kruskal");
            System.out.println("12. Gerar MST com Boruvka");
            System.out.println("13. Gerar Ciclo Mínimo com Base na MST");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
        }

        private static void carregarGrafo() {
            String caminhoArquivo = "Atividade\\Arquivos\\Arquivo";
            try {
                grafo.lerGrafoDeArquivo(caminhoArquivo);
                System.out.println("Grafo carregado com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        }

        private static void adicionarVertice() {
            System.out.print("Digite o nome do vértice: ");
            String vertice = scanner.nextLine();
            grafo.adicionarVertice(vertice);
            System.out.println("Vértice adicionado com sucesso!");
        }

        private static void removerVertice() {
            System.out.print("Digite o nome do vértice a ser removido: ");
            String vertice = scanner.nextLine();
            grafo.removerVertice(vertice);
            System.out.println("Vértice removido com sucesso!");
        }

        private static void adicionarAresta() {
            System.out.print("Digite o vértice de início: ");
            String inicio = scanner.nextLine();
            System.out.print("Digite o vértice de fim: ");
            String fim = scanner.nextLine();
            System.out.print("Digite o peso da aresta: ");
            double peso = scanner.nextDouble();
            scanner.nextLine(); // Consumir a nova linha

            grafo.adicionarAresta(peso, inicio, fim);
            System.out.println("Aresta adicionada com sucesso!");
        }

        private static void removerAresta() {
            System.out.print("Digite o vértice de início: ");
            String inicio = scanner.nextLine();
            System.out.print("Digite o vértice de fim: ");
            String fim = scanner.nextLine();

            grafo.removerAresta(inicio, fim);
            System.out.println("Aresta removida com sucesso!");
        }

        private static void buscaEmLargura() {
            System.out.println("Executando busca em largura (BFS):");
            grafo.buscaEmLargura();
        }

        private static void buscaEmProfundidade() {
            System.out.println("Executando busca em profundidade (DFS):");
            grafo.executarDFS();
        }

        private static void imprimirTemposDFS() {
            System.out.println("Imprimindo tempos de chegada e partida (DFS):");
            grafo.imprimirTempos();
        }

        private static void verificarBipartido() {
            System.out.println("Verificando se o grafo é bipartido:");
            grafo.verificarBipartido();
        }

        private static void encontrarVR() {
            System.out.println("Verificando se há um vértice raiz (VR):");
            grafo.encontrarVR();
        }

        private static void executarPrim() {
            System.out.println("Gerando Árvore Geradora Mínima com Prim:");
            List<Aresta<String>> mst = grafo.algoritmoDePrim();
            grafo.imprimirMST(mst);
        }

        private static void executarKruskal() {
            System.out.println("Gerando Árvore Geradora Mínima com Kruskal:");
            List<Aresta<String>> mst = grafo.algoritmoDeKruskal();
            grafo.imprimirMST(mst);
        }

        private static void executarBoruvka() {
            System.out.println("Gerando Árvore Geradora Mínima com Boruvka:");
            List<Aresta<String>> mst = grafo.algoritmoDeBoruvka();
            grafo.imprimirMST(mst);
        }

        private static void gerarCicloMinimo() {
            System.out.println("Gerando Ciclo Mínimo com base na MST:");
            List<String> ciclo = grafo.gerarCicloMinimo();
            grafo.imprimirCiclo(ciclo);
        }
    }
}
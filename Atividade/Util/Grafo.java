package Atividade.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grafo<TIPO> {
    private ArrayList<Vertice<TIPO>> vertices;
    private ArrayList<Aresta<TIPO>> arestas;
    private int tempo;
    private int[] tempoChegada;
    private int[] tempoPartida;

    public Grafo() {
        this.vertices = new ArrayList<>();
        this.arestas = new ArrayList<>();
        this.tempo = 0;
    }

    public void adicionarVertice(TIPO dado){
        Vertice<TIPO> novoVertice = new Vertice<TIPO>(dado);
        this.vertices.add(novoVertice);
    }

    public void removerVertice(TIPO dado) {
        Vertice<TIPO> vertice = this.getVertice(dado);
        if (vertice != null) {
            // Remover as arestas associadas ao vértice
            this.arestas.removeIf(aresta -> aresta.getInicio().equals(vertice) || aresta.getFim().equals(vertice));

            // Remover o vértice em si
            this.vertices.remove(vertice);
        }
    }

    public void adicionarAresta(Double peso, TIPO dadoInicio, TIPO dadoFim){
        Vertice<TIPO> inicio = this.getVertice(dadoInicio);
        Vertice<TIPO> fim = this.getVertice(dadoFim);
        Aresta<TIPO> aresta = new Aresta<TIPO>(peso, inicio, fim);
        inicio.adicionarArestaSaida(aresta);
        fim.adicionarArestaEntrada(aresta);
        this.arestas.add(aresta);
    }

    public void removerAresta(TIPO dadoInicio, TIPO dadoFim) {
        Vertice<TIPO> inicio = this.getVertice(dadoInicio);
        Vertice<TIPO> fim = this.getVertice(dadoFim);
        if (inicio != null && fim != null) {
            this.arestas.removeIf(aresta -> aresta.getInicio().equals(inicio) && aresta.getFim().equals(fim));
            inicio.getArestasSaida().removeIf(aresta -> aresta.getFim().equals(fim));
            fim.getArestasEntrada().removeIf(aresta -> aresta.getInicio().equals(inicio));
        }
    }

    public boolean pesquisarVertice(TIPO dado) {
        return this.getVertice(dado) != null;
    }

    public boolean pesquisarAresta(TIPO dadoInicio, TIPO dadoFim) {
        Vertice<TIPO> inicio = this.getVertice(dadoInicio);
        Vertice<TIPO> fim = this.getVertice(dadoFim);
        if (inicio != null && fim != null) {
            for (Aresta<TIPO> aresta : this.arestas) {
                if (aresta.getInicio().equals(inicio) && aresta.getFim().equals(fim)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Vertice<TIPO>> obterAdjacentes(TIPO dado) {
        Vertice<TIPO> vertice = this.getVertice(dado);
        ArrayList<Vertice<TIPO>> adjacentes = new ArrayList<>();
        if (vertice != null) {
            for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
                adjacentes.add(aresta.getFim());
            }
        }
        return adjacentes;
    }

    public void imprimirGrafo() {
        for (Vertice<TIPO> vertice : this.vertices) {
            System.out.print(vertice.getDado() + " -> ");
            for (Vertice<TIPO> adjacente : this.obterAdjacentes(vertice.getDado())) {
                System.out.print(adjacente.getDado() + " ");
            }
            System.out.println();
        }
    }

    public void inicializarTempos() {
        int n = this.vertices.size();
        this.tempoChegada = new int[n];
        this.tempoPartida = new int[n];
        for (int i = 0; i < n; i++) {
            this.tempoChegada[i] = -1;
            this.tempoPartida[i] = -1;
        }
    }

    public void imprimirTempos() {
        for (int i = 0; i < vertices.size(); i++) {
            System.out.println(vertices.get(i).getDado() + " (Chegada: " + tempoChegada[i] + ", Partida: " + tempoPartida[i] + ")");
        }
    }

    public ArrayList<Vertice<TIPO>> getVertices() {
        return this.vertices;
    }

    public Vertice<TIPO> getVertice(TIPO dado){
        Vertice<TIPO> vertice = null;
        for(int i=0; i < this.vertices.size(); i++){
            if (this.vertices.get(i).getDado().equals(dado)){
                vertice = this.vertices.get(i);
                break;
            }
        }
        return vertice;
    }

    public void buscaEmLargura(){
        ArrayList<Vertice<TIPO>> marcados = new ArrayList<Vertice<TIPO>>();
        ArrayList<Vertice<TIPO>> fila = new ArrayList<Vertice<TIPO>>();
        Vertice<TIPO> atual = this.vertices.get(0);
        marcados.add(atual);
        System.out.println(atual.getDado());
        fila.add(atual);
        while(fila.size() > 0){
            Vertice<TIPO> visitado = fila.get(0);
            for(int i=0; i < visitado.getArestasSaida().size(); i++){
                Vertice<TIPO> proximo = visitado.getArestasSaida().get(i).getFim();
                if (!marcados.contains(proximo)){ //se o vértice ainda não foi marcado
                    marcados.add(proximo);
                    System.out.println(proximo.getDado());
                    fila.add(proximo);
                }
            }
            fila.remove(0);
        }
    }

    // Método para executar DFS e calcular os tempos de chegada e partida
    public void executarDFS() {
        inicializarTempos(); // Inicializa os arrays de tempos
        this.tempo = 0;

        for (int i = 0; i < vertices.size(); i++) {
            if (tempoChegada[i] == -1) {
                MétodoRecursivoDFS(i);
            }
        }
    }

    // Método recursivo para DFS
    private void MétodoRecursivoDFS(int indiceVertice) {
        tempo++;
        tempoChegada[indiceVertice] = tempo;

        Vertice<TIPO> vertice = vertices.get(indiceVertice);
        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            int indiceProximo = vertices.indexOf(aresta.getFim());
            if (tempoChegada[indiceProximo] == -1) {
                MétodoRecursivoDFS(indiceProximo);
            }
        }

        tempo++;
        tempoPartida[indiceVertice] = tempo;
    }

    // Método para verificar se um vértice é raiz
    private boolean VerificarSeÉRaiz(int indiceVertice) {
        // Realizar DFS a partir deste vértice para verificar se ele alcança todos os outros
        ArrayList<Integer> visitados = new ArrayList<>();
        dfsVerificarRaiz(indiceVertice, visitados);

        return visitados.size() == vertices.size();
    }

    // Método para encontrar e imprimir o VR
    public void encontrarVR() {
        // Executar DFS para obter os tempos de partida
        executarDFS();

        // Encontrar o vértice com o maior tempo de partida
        int candidatoVR = -1;
        int maiorTempoDePartida = -1;

        for (int i = 0; i < vertices.size(); i++) {
            if (tempoPartida[i] > maiorTempoDePartida) {
                maiorTempoDePartida = tempoPartida[i];
                candidatoVR = i;
            }
        }

        // Verificar se o candidato a VR pode alcançar todos os vértices
        if (candidatoVR != -1) {
            ArrayList<Integer> visitados = new ArrayList<>();
            dfsVerificarRaiz(candidatoVR, visitados);

            if (visitados.size() == vertices.size()) {
                System.out.println("O VR é : Vértice " + vertices.get(candidatoVR).getDado());
            } else {
                System.out.println("O grafo não possui VR");
            }
        } else {
            System.out.println("O grafo não possui VR");
        }
    }

    // Método auxiliar para DFS a partir do candidato a VR
    private void dfsVerificarRaiz(int indiceVertice, ArrayList<Integer> visitados) {
        visitados.add(indiceVertice);
        Vertice<TIPO> vertice = vertices.get(indiceVertice);

        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            int indiceProximo = vertices.indexOf(aresta.getFim());
            if (!visitados.contains(indiceProximo)) {
                dfsVerificarRaiz(indiceProximo, visitados);
            }
        }
    }

    private Vertice<TIPO> encontrarRaiz(Map<Vertice<TIPO>, Vertice<TIPO>> pais, Vertice<TIPO> vertice) {
        while (!pais.get(vertice).equals(vertice)) {
            vertice = pais.get(vertice);
        }
        return vertice;
    }

    // Método para verificar se o grafo é bipartido usando DFS
    public boolean verificarBipartido() {
        int n = vertices.size();
        int[] cores = new int[n];
        Arrays.fill(cores, -1); // -1 significa que o vértice ainda não foi colorido

        for (int i = 0; i < n; i++) {
            if (cores[i] == -1) { // Se o vértice não foi colorido, execute DFS a partir dele
                if (!dfsVerificarBipartido(i, 0, cores)) {
                    System.out.println("O grafo NÃO é bipartido.");
                    return false;
                }
            }
        }

        // Se chegou até aqui, o grafo é bipartido
        imprimirParticoes(cores);
        return true;
    }

    // Método recursivo DFS para verificar e colorir o grafo
    private boolean dfsVerificarBipartido(int indiceVertice, int cor, int[] cores) {
        cores[indiceVertice] = cor;

        Vertice<TIPO> vertice = vertices.get(indiceVertice);
        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            int indiceProximo = vertices.indexOf(aresta.getFim());

            if (cores[indiceProximo] == -1) { // Se o próximo vértice não foi colorido
                if (!dfsVerificarBipartido(indiceProximo, 1 - cor, cores)) {
                    return false;
                }
            } else if (cores[indiceProximo] == cores[indiceVertice]) { // Se o próximo vértice tem a mesma cor
                return false;
            }
        }
        return true;
    }

    // Método para imprimir as partições se o grafo for bipartido
    private void imprimirParticoes(int[] cores) {
        ArrayList<TIPO> particao1 = new ArrayList<>();
        ArrayList<TIPO> particao2 = new ArrayList<>();

        for (int i = 0; i < cores.length; i++) {
            if (cores[i] == 0) {
                particao1.add(vertices.get(i).getDado());
            } else {
                particao2.add(vertices.get(i).getDado());
            }
        }

        System.out.println("O grafo É bipartido: Partição 1 " + particao1 + " e Partição 2 " + particao2);
    }

    // Algoritmo de Prim
    public List<Aresta<TIPO>> algoritmoDePrim() {
        // Lista que armazenará as arestas da árvore geradora mínima (MST)
        List<Aresta<TIPO>> mst = new ArrayList<>();

        // Array para indicar se um vértice está na árvore
        boolean[] naArvore = new boolean[vertices.size()];

        // Inicializa o primeiro vértice (inicialmente na posição 0) como na árvore
        naArvore[0] = true;

        // Enquanto o número de arestas na MST for menor que o número de vértices - 1
        while (mst.size() < vertices.size() - 1) {
            Aresta<TIPO> menorAresta = null;
            int verticeEscolhido = -1;

            // Para cada vértice que já está na árvore, encontramos a menor aresta que conecta a árvore a um novo vértice
            for (int i = 0; i < vertices.size(); i++) {
                if (naArvore[i]) {
                    Vertice<TIPO> vertice = vertices.get(i);
                    for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
                        int indiceDestino = vertices.indexOf(aresta.getFim());

                        // Se o vértice destino ainda não estiver na árvore e a aresta é a menor até agora
                        if (!naArvore[indiceDestino]) {
                            if (menorAresta == null || aresta.getPeso() < menorAresta.getPeso()) {
                                menorAresta = aresta;
                                verticeEscolhido = indiceDestino;
                            }
                        }
                    }
                }
            }

            // Se encontramos uma aresta válida, adicionamos à MST e marcamos o novo vértice
            if (menorAresta != null && verticeEscolhido != -1) {
                mst.add(menorAresta);
                naArvore[verticeEscolhido] = true; // Marca o vértice como parte da árvore
            } else {
                break; // Não existem mais arestas válidas para adicionar, o algoritmo termina
            }
        }

        return mst;
    }

    // Algoritmo de Kruskal
    public List<Aresta<TIPO>> algoritmoDeKruskal() {
        List<Aresta<TIPO>> mst = new ArrayList<>();

        // Ordena as arestas pelo peso
        Collections.sort(arestas, Comparator.comparing(Aresta::getPeso));

        // Array para rastrear o conjunto ao qual cada vértice pertence (a "floresta")
        int[] conjuntos = new int[vertices.size()];

        // Inicialmente, cada vértice pertence ao seu próprio conjunto (árvore independente)
        for (int i = 0; i < vertices.size(); i++) {
            conjuntos[i] = i; // Cada vértice é o representante de seu próprio conjunto
        }

        // Processa cada aresta, adicionando-a à MST se não formar um ciclo
        for (Aresta<TIPO> aresta : arestas) {
            int indiceU = vertices.indexOf(aresta.getInicio());
            int indiceV = vertices.indexOf(aresta.getFim());

            int conjuntoU = encontrar(conjuntos, indiceU);
            int conjuntoV = encontrar(conjuntos, indiceV);

            // Se os vértices pertencem a conjuntos diferentes, adicione a aresta à MST
            if (conjuntoU != conjuntoV) {
                mst.add(aresta);
                unir(conjuntos, conjuntoU, conjuntoV);
            }

            // Se já temos n-1 arestas na MST, podemos parar
            if (mst.size() == vertices.size() - 1) {
                break;
            }
        }

        return mst;
    }

    // Algoritmo de Boruvka
    public List<Aresta<TIPO>> algoritmoDeBoruvka() {
        List<Aresta<TIPO>> mst = new ArrayList<>();

        // Array para rastrear o componente ao qual cada vértice pertence
        int[] componentes = new int[vertices.size()];

        // Inicializa cada vértice como seu próprio componente
        for (int i = 0; i < vertices.size(); i++) {
            componentes[i] = i;  // Cada vértice é o representante de seu próprio componente
        }

        int componentesRestantes = vertices.size();

        // Enquanto existir mais de um componente
        while (componentesRestantes > 1) {
            Aresta<TIPO>[] menorArestaPorComponente = new Aresta[vertices.size()];

            // Encontrar a menor aresta que conecta dois componentes diferentes
            for (Aresta<TIPO> aresta : arestas) {
                int componenteU = encontrar(componentes, vertices.indexOf(aresta.getInicio()));
                int componenteV = encontrar(componentes, vertices.indexOf(aresta.getFim()));

                if (componenteU != componenteV) {
                    // Verifica e armazena a menor aresta para cada componente
                    if (menorArestaPorComponente[componenteU] == null || aresta.getPeso() < menorArestaPorComponente[componenteU].getPeso()) {
                        menorArestaPorComponente[componenteU] = aresta;
                    }
                    if (menorArestaPorComponente[componenteV] == null || aresta.getPeso() < menorArestaPorComponente[componenteV].getPeso()) {
                        menorArestaPorComponente[componenteV] = aresta;
                    }
                }
            }

            // Adicionar as arestas de menor custo à MST e unir os componentes
            for (Aresta<TIPO> aresta : menorArestaPorComponente) {
                if (aresta != null) {
                    int componenteU = encontrar(componentes, vertices.indexOf(aresta.getInicio()));
                    int componenteV = encontrar(componentes, vertices.indexOf(aresta.getFim()));

                    if (componenteU != componenteV) {
                        mst.add(aresta);
                        unir(componentes, componenteU, componenteV);
                        componentesRestantes--;
                    }
                }
            }
        }

        return mst;
    }

    // Método auxiliar para imprimir as arestas da MST
    public void imprimirMST(List<Aresta<TIPO>> mst) {
        for (Aresta<TIPO> aresta : mst) {
            System.out.println(aresta.getInicio().getDado() + " -- " + aresta.getFim().getDado() + " : " + aresta.getPeso());
        }
    }

    public List<TIPO> gerarCicloMinimo() {
        List<Aresta<TIPO>> mst = this.algoritmoDePrim(); // Use o algoritmo que preferir
        Grafo<TIPO> mstGrafo = new Grafo<>();
        for (Aresta<TIPO> aresta : mst) {
            mstGrafo.adicionarVertice(aresta.getInicio().getDado());
            mstGrafo.adicionarVertice(aresta.getFim().getDado());
            mstGrafo.adicionarAresta(aresta.getPeso(), aresta.getInicio().getDado(), aresta.getFim().getDado());
        }

        List<TIPO> ciclo = new ArrayList<>();
        Set<TIPO> visitados = new HashSet<>();
        Vertice<TIPO> verticeInicial = mstGrafo.getVertices().get(0);
        gerarCicloDFS(verticeInicial, ciclo, visitados);
        ciclo.add(verticeInicial.getDado()); // Fechar o ciclo

        return ciclo;
    }

    private void gerarCicloDFS(Vertice<TIPO> vertice, List<TIPO> ciclo, Set<TIPO> visitados) {
        visitados.add(vertice.getDado());
        ciclo.add(vertice.getDado());

        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            Vertice<TIPO> proximo = aresta.getFim();
            if (!visitados.contains(proximo.getDado())) {
                gerarCicloDFS(proximo, ciclo, visitados);
            }
        }
    }

    public void imprimirCiclo(List<TIPO> ciclo) {
        for (int i = 0; i < ciclo.size() - 1; i++) {
            System.out.println(ciclo.get(i) + " -> " + ciclo.get(i + 1));
        }
    }

    public void lerGrafoDeArquivo(String caminhoArquivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
        String linha;

        while ((linha = reader.readLine()) != null) {
            linha = linha.trim(); // Remove espaços em branco no início e no final
            if (linha.isEmpty()) {
                continue; // Ignora linhas vazias
            }

            String[] partes = linha.split(";");
            if (partes.length != 3) {
                System.err.println("Linha malformada: " + linha);
                continue; // Ignora linhas malformadas
            }

            TIPO verticeA = (TIPO) partes[0];
            TIPO verticeB = (TIPO) partes[1];
            double peso = Double.parseDouble(partes[2]);

            adicionarVertice(verticeA);
            adicionarVertice(verticeB);
            adicionarAresta(peso, verticeA, verticeB);
        }
        reader.close();
    }

    // Função auxiliar para encontrar o representante do conjunto/componente ao qual um vértice pertence
    private int encontrar(int[] representante, int vertice) {
        // Segue o encadeamento até encontrar o representante do conjunto/componente
        if (representante[vertice] != vertice) {
            representante[vertice] = encontrar(representante, representante[vertice]); // Compressão de caminho
        }
        return representante[vertice];
    }

    // Função auxiliar para unir conjuntos/componentes
    private void unir(int[] representante, int conjuntoU, int conjuntoV) {
        // Unir todos os vértices do conjunto/componente V ao conjunto/componente U
        for (int i = 0; i < representante.length; i++) {
            if (representante[i] == conjuntoV) {
                representante[i] = conjuntoU;
            }
        }
    }
}
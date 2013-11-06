
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import static java.lang.System.out;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Carlos Basso
 */
public class MDP {

    /**
     * @param args the command line arguments
     */
    double Matriz[][];
    int n;
    int m;
    double MAX = 0;
    ArrayList<Integer> sol = new ArrayList<Integer>();
    ArrayList<Integer> unos = new ArrayList<Integer>();
    ArrayList<Integer> ceros = new ArrayList<Integer>();

    public void leer_fichero_datos(String fichero) {

        FileReader entrada = null;
        StringBuffer str = new StringBuffer();
        int j = 0, i = 0;
        char aux[] = new char[10];
        try {
            entrada = new FileReader(fichero);
            int c;
            while ((c = entrada.read()) != -1) {
                str.append((char) c);
            }
        } catch (IOException ex) {
            System.out.println("\nNo se ha encontrado el archivo de texto que contiene los datos del problema.");
        }
        while (str.charAt(j) == ' ') {
            j++;
        }

        while (str.charAt(j) != ' ') {
            aux[i] = str.charAt(j);
            j++;
            i++;
        }
        String aux_str = new String(aux);
        n = Integer.parseInt(aux_str.substring(0, i));

        while (str.charAt(j) == ' ' || str.charAt(j) == '\n') {
            j++;
        }

        aux = new char[10];
        i = 0;
        //while(str.charAt(j) !=' ' && str.charAt(j) !='\n' && str.charAt(j) !='\t'){
        while (Character.isDigit(str.charAt(j))) {
            aux[i] = str.charAt(j);
            j++;
            i++;
        }
        aux_str = new String(aux);
        m = Integer.parseInt(aux_str.substring(0, i));
        Matriz = new double[n][n];
        while (str.charAt(j) == ' ' || str.charAt(j) == '\n') {
            j++;
        }
        int k = j;
        for (int pos = 0; pos < n; pos++) {
            Matriz[pos][pos] = 0;
        }
        for (int pos1 = 0; pos1 < n - 1; pos1++) {
            for (int pos2 = pos1 + 1; pos2 < n; pos2++) {
                int cont = 0;
                while (str.charAt(k) == '\n') {
                    k++;
                }
                while (cont != 2) {
                    if (str.charAt(k) == ' ') {
                        cont++;
                    }
                    k++;
                }
                aux = new char[20];
                i = 0;
                while (str.charAt(k) != ' ' && str.charAt(k) != '\n') {
                    aux[i] = str.charAt(k);
                    k++;
                    i++;
                }
                aux_str = new String(aux);
                Matriz[pos1][pos2] = Double.parseDouble(aux_str.substring(0, i));
                Matriz[pos2][pos1] = Double.parseDouble(aux_str.substring(0, i));
            }
        }
    }

    public void acciones() {
        String cadena = "";
        try {
            Scanner scanerDD = new Scanner(new File("datos.txt"));
            int ALG = scanerDD.nextInt();
            int Semilla = scanerDD.nextInt();
            int Fichero = scanerDD.nextInt();
            ArrayList<Integer> Semillas = new ArrayList<Integer>();
            ArrayList<String> Ficheros = new ArrayList<String>();

            for (int i = 0; i < Semilla; i = i + 1) {
                Semillas.add(scanerDD.nextInt());
            }
            for (int i = 0; i < Fichero + 1; i = i + 1) {
                Ficheros.add(scanerDD.nextLine());
            }
            Ficheros.remove("\t");
            Ficheros.remove("\n");
            Ficheros.remove("");

            if (ALG == 0) {
                System.out.print("Busqueda Multiarranque Basica \n");
                cadena = "Busqueda Multiarranque Basica \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.MBM(numero);
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.MAX);
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("BMB.txt");
                scribirDD.append("Algoritmo BMB:\n");
                scribirDD.append(cadena);
            }

            if (ALG == 1) {
                System.out.print("GRASP \n");
                cadena = "GRASP \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.GRAPS(numero);
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.MAX);
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("GRASP.txt");
                scribirDD.append("Algoritmo GRASP:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 2) {
                 System.out.print("Greedy mas busqueda local \n");
                cadena = "Greedy mas busqueda local  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    this.GreedyBL();
                    System.out.print("\nMAXIMO: ");
                    System.out.print(this.MAX);
                    cadena = cadena + "\t\tMaximo: " + this.MAX + "\n";

                }
                PrintStream scribirDD = new PrintStream("GreedyBL.txt");
                scribirDD.append("Greedy mas Busqueda local:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 3) {
                cadena = "GRASP EXTENDIDO \n";
                System.out.print("GRASP EXTENDIDO \n");
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.GRAPS_Ex(numero);
                        System.out.print(numero);
                        System.out.print("\tMAXIMO: ");
                        System.out.print(this.MAX);
                        System.out.print("\n");
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("GRASPextendido.txt");
                scribirDD.append("Algoritmo GRASP-Extendido:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 4) {
                System.out.print("Greedy mas busqueda local extendida \n");
                cadena = "Greedy + BL extendida \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.GreedyBL_Ex(numero);
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.MAX);
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("GreedyBL_Ex.txt");
                scribirDD.append("Algoritmo Gredy BL Extendida:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 5) {
                System.out.print("ILS \n");
                cadena = "ILS \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.ILS(Semilla);
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.MAX);
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("ILS.txt");
                scribirDD.append("Algoritmo ILS:\n");
                scribirDD.append(cadena);
            }

            if (ALG == 6) {
                System.out.print("VNS \n");
                cadena = "VNS \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        this.VNS(Semilla);
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.MAX);
                        cadena = cadena + "\t\t\tMaximo: " + this.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("VNS.txt");
                scribirDD.append("Algoritmo VNS:\n");
                scribirDD.append(cadena);
            }


        } catch (IOException ex) {
            System.out.println("\nNo se ha encontrado el archivo de texto CON LAS ACCIONES PARA EL PROBLEMA.");
        }
    }

    public void LimpiarSolucion() {
        unos.clear();
        ceros.clear();
        sol.clear();
        MAX = 0;
        for (int i = 0; i < n; i = i + 1) {
            ceros.add(i);
            sol.add(0);
        }
    }

    public int BuscarMAsDiverso() {
        double max = 0;
        int sal = 0;
        for (int i = 0; i < n; i = i + 1) {
            double cont = 0;
            for (int s = 0; s < n; s = s + 1) {
                if (i < s) {
                    cont = Matriz[i][s] + cont;
                }
                if (i > s) {
                    cont = Matriz[s][i] + cont;
                }
            }
            if (max < cont) {
                sal = i;
                max = cont;
            }
        }
        return sal;
    }

    public int Buscar() {
        double max = 0;
        int pos = 0;
        for (int i = 0; i < ceros.size(); i = i + 1) {
            double aux = 0;
            for (int s = 0; s < unos.size(); s = s + 1) {
                if (ceros.get(i) < unos.get(s)) {
                    aux = Matriz[ceros.get(i)][unos.get(s)] + aux;
                }
                if (ceros.get(i) > unos.get(s)) {
                    aux = Matriz[unos.get(s)][ceros.get(i)] + aux;
                }
            }
            if (max < aux) {
                pos = i;
                max = aux;
            }
        }
        return pos;
    }

    public int BuscarProbablidad(int l, Random a) {
        double max = 0;
        ArrayList<Integer> listaMax = new ArrayList<Integer>();
        int pos = 0;
        ArrayList<Integer> AuxCeros = (ArrayList<Integer>) ceros.clone();
        ArrayList<Integer> AuxUnos = (ArrayList<Integer>) unos.clone();
        for (int j = 0; j < l; j = j + 1) {
            for (int i = 0; i < AuxCeros.size(); i = i + 1) {
                double aux = 0;
                for (int s = 0; s < AuxUnos.size(); s = s + 1) {
                    if (AuxCeros.get(i) < AuxUnos.get(s)) {
                        aux = Matriz[AuxCeros.get(i)][AuxUnos.get(s)] + aux;
                    }
                    if (AuxCeros.get(i) > AuxUnos.get(s)) {
                        aux = Matriz[AuxUnos.get(s)][AuxCeros.get(i)] + aux;
                    }
                }
                if (max < aux) {
                    listaMax.add(i);
                    AuxCeros.remove(i);
                }
            }
        }
        pos = listaMax.get(a.nextInt(listaMax.size()));
        return pos;
    }

    public double SolucionAleatoria(Random a) {
        this.LimpiarSolucion();
        for (int i = 0; i < m; i = i + 1) {
            int numero = a.nextInt(this.ceros.size());
            int cero = ceros.remove(numero);
            unos.add(cero);
        }
        this.FuncionObjetivo();
        return MAX;
    }

    public double Gredy() {
        this.LimpiarSolucion();
        int masdiverso;
        masdiverso = this.BuscarMAsDiverso();
        unos.add(masdiverso);
        ceros.remove(masdiverso);
        for (int i = 1; i < m; i = i + 1) {
            int aux = this.Buscar();
            int cero = ceros.remove(aux);
            unos.add(cero);
//for y in range(len(self.Lista1)):
//self.sol[self.Lista1[y]]=1       
        }
        this.FuncionObjetivo();
        return this.MAX;
    }

    public double GredyND(Random a, int l) {
        this.LimpiarSolucion();
        int siguiente = a.nextInt(ceros.size());
        unos.add(siguiente);
        ceros.remove(siguiente);
        for (int i = 1; i < m; i = i + 1) {
            siguiente = this.BuscarProbablidad(l, a);
            int cero = ceros.remove(siguiente);
            unos.add(cero);
        }
        this.FuncionObjetivo();
        return this.MAX;
    }

    public double FuncionObjetivo() {
        double c = 0;
        if (unos.size() > m) {
            System.out.print(" error mas elementos");
        }
        if (unos.size() < m) {
            System.out.print("error menos elementos");
        }
        for (int i = 0; i < unos.size(); i = i + 1) {
            for (int s = i; s < unos.size(); s = s + 1) {
                if (unos.get(i) != unos.get(s)) {
                    if (unos.get(i) < unos.get(s)) {
                        c = c + Matriz[unos.get(i)][unos.get(s)];
                    } else {
                        c = c + Matriz[unos.get(s)][unos.get(i)];
                    }
                }
            }
        }
        this.MAX = c;
        return c;
    }

    public double recalcula_diversidad(int eliminado, int nuevo) {

        for (int i = 0; i < m; i++) {
            if (unos.get(i) != nuevo) {
                MAX = MAX - Matriz[eliminado][unos.get(i)];
            }
            MAX = MAX + Matriz[nuevo][unos.get(i)];
        }/*
         for (int i = 0; i < m; i++) {
         MAX = MAX + Matriz[nuevo][unos.get(i)];
         }*/
        return MAX;
    }

    public double BL(boolean Flag1, int semilla) {

        if (Flag1 == true) {
            Random rnd = new Random(semilla);
            this.LimpiarSolucion();
            this.SolucionAleatoria(rnd);
        }
        ArrayList<Integer> auxiliarCeros = (ArrayList<Integer>) ceros.clone();
        ArrayList<Integer> auxiliarunos = (ArrayList<Integer>) unos.clone();
        //empezamos en si la busqueda local
        this.FuncionObjetivo();
        boolean mejora = true;
        double max2 = this.MAX;
        while (mejora == true) {
            mejora = false;
            Collections.shuffle(unos);

            for (int i = 0; i < unos.size() && mejora == false; i = i + 1) {
                for (int j = 0; j < ceros.size() && mejora == false; j = j + 1) {
                    double MAX2 = this.MAX;
                    int aux2 = ceros.get(j);
                    int aux = unos.get(i);
                    ceros.remove(j);
                    unos.remove(i);
                    unos.add(aux2);
                    ceros.add(aux);
                    this.recalcula_diversidad(aux, aux2);
                    if (this.MAX > max2) {
                        mejora = true;
                        auxiliarunos = (ArrayList<Integer>) this.unos.clone();
                        auxiliarCeros = (ArrayList<Integer>) this.ceros.clone();
                        max2 = this.MAX;

                    } else {
                        unos = (ArrayList<Integer>) auxiliarunos.clone();
                        ceros = (ArrayList<Integer>) auxiliarCeros.clone();
                        this.MAX = MAX2;
                    }
                }
            }
        }
        this.unos = (ArrayList<Integer>) auxiliarunos.clone();
        this.ceros = (ArrayList<Integer>) auxiliarCeros.clone();
        this.FuncionObjetivo();
        return this.MAX;

    }

    public double GreedyBL() {
        this.Gredy();
        this.BL(false, 123456789);
        return this.MAX;
    }

    public double MBM(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        this.SolucionAleatoria(rnd);

        ArrayList<Integer> mejorCeros = (ArrayList<Integer>) ceros.clone();
        ArrayList<Integer> mejorUnos = (ArrayList<Integer>) unos.clone();
        double maximo = 0;
        double diver;
        for (int varias = 0; varias < 25; varias = varias + 1) {
            this.LimpiarSolucion();
            this.SolucionAleatoria(rnd);
            diver = this.BL(false, 0);
            if (diver > maximo) {
                maximo = diver;
                mejorCeros = (ArrayList<Integer>) ceros.clone();
                mejorUnos = (ArrayList<Integer>) unos.clone();
            }
            this.SolucionAleatoria(rnd);

        }
        unos = (ArrayList<Integer>) mejorUnos.clone();
        ceros = (ArrayList<Integer>) mejorCeros.clone();
        MAX = maximo;
        return MAX;
    }

    public double GRAPS(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        double maximo = this.GredyND(rnd, 5);
        ArrayList<Integer> mejorUnos = (ArrayList<Integer>) unos.clone();
        ArrayList<Integer> mejorCeros = (ArrayList<Integer>) ceros.clone();
        for (int recore = 0; recore < 25; recore = recore + 1) {
            this.BL(false, 0);
            if (this.MAX > maximo) {
                maximo = this.MAX;
                mejorUnos = (ArrayList<Integer>) unos.clone();
                mejorCeros = (ArrayList<Integer>) ceros.clone();
            }
            this.GredyND(rnd, 5);
        }
        unos = (ArrayList<Integer>) mejorUnos.clone();
        ceros = (ArrayList<Integer>) mejorCeros.clone();
        MAX = maximo;
        return MAX;
    }

    double Mutacion(int s, Random r) {
        ArrayList<Integer> Unoscam = new ArrayList<Integer>();
        ArrayList<Integer> Ceroscam = new ArrayList<Integer>();
        for (int recore = 0; recore < s; recore = recore + 1) {
            int numeroUno = r.nextInt(unos.size());
            int numeroCero = r.nextInt(ceros.size());
            Unoscam.add(numeroUno);
            Ceroscam.add(numeroCero);
            unos.remove(numeroUno);
            ceros.remove(numeroCero);

        }
        for (int recore = 0; recore < s; recore = recore + 1) {
            unos.add(Unoscam.remove(recore));
            ceros.add(Ceroscam.remove(recore));
        }

        return this.FuncionObjetivo();
    }

    public double GRAPS_Ex(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        double maximo = this.GredyND(rnd, 7);
        ArrayList<Integer> mejorUnos = (ArrayList<Integer>) unos.clone();
        ArrayList<Integer> mejorCeros = (ArrayList<Integer>) ceros.clone();
        for (int recore = 0; recore < 5; recore = recore + 1) {
            for (int auxiliar = 0; auxiliar < 5; auxiliar = auxiliar + 1) {
                this.BL(false, 0);
                if (this.MAX > maximo) {
                    maximo = this.MAX;
                    mejorUnos = (ArrayList<Integer>) unos.clone();
                    mejorCeros = (ArrayList<Integer>) ceros.clone();
                }
                int s = (int) 0.10 * m;
                this.Mutacion(s, rnd);
                if (this.MAX > maximo) {
                    maximo = this.MAX;
                    mejorUnos = (ArrayList<Integer>) unos.clone();
                    mejorCeros = (ArrayList<Integer>) ceros.clone();
                }
            }
            this.GredyND(rnd, 5);

        }
        unos = (ArrayList<Integer>) mejorUnos.clone();
        ceros = (ArrayList<Integer>) mejorCeros.clone();
        MAX = maximo;
        return MAX;
    }

    public double GreedyBL_Ex(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        double maximo = this.Gredy();
        ArrayList<Integer> mejorUnos = (ArrayList<Integer>) unos.clone();
        ArrayList<Integer> mejorCeros = (ArrayList<Integer>) ceros.clone();
        for (int recore = 0; recore < 5; recore = recore + 1) {
            int s = (int) 0.1 * m;
            Collections.shuffle(unos, rnd);
            Collections.shuffle(ceros, rnd);
            this.Mutacion(s, rnd);
            this.BL(false, 0);
            if (this.MAX > maximo) {
                maximo = this.MAX;
                mejorUnos = (ArrayList<Integer>) unos.clone();
                mejorCeros = (ArrayList<Integer>) ceros.clone();
            }
            this.Gredy();
        }
        unos = (ArrayList<Integer>) mejorUnos;
        ceros = (ArrayList<Integer>) mejorCeros;
        this.FuncionObjetivo();
        return MAX;
    }

    public double ILS(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        int s = (int) 0.10 * m;
        double maximo = this.SolucionAleatoria(rnd);
        ArrayList<Integer> mejorUnos = (ArrayList<Integer>) unos.clone();
        ArrayList<Integer> mejorCeros = (ArrayList<Integer>) ceros.clone();

        this.BL(false, 0);
        for (int r = 0; r < 24; r = r + 1) {
            this.Mutacion(s, rnd);
            this.BL(false, 0);
            if (this.MAX > maximo) {
                maximo = this.MAX;
                mejorUnos = (ArrayList<Integer>) unos.clone();
                mejorCeros = (ArrayList<Integer>) ceros.clone();
            }
        }
        unos = (ArrayList<Integer>) mejorUnos;
        ceros = (ArrayList<Integer>) mejorCeros;
        this.FuncionObjetivo();

        return MAX;
    }

    public double GenerarVecino(int s, Random r) {
        ArrayList<Integer> Unoscam = new ArrayList<Integer>();
        ArrayList<Integer> Ceroscam = new ArrayList<Integer>();

        for (int recore = 0; recore < s; recore = recore + 1) {
            int numeroUno = r.nextInt(unos.size());
            int numeroCero = r.nextInt(ceros.size());
            Unoscam.add(numeroUno);
            Ceroscam.add(numeroCero);
            unos.remove(numeroUno);
            ceros.remove(numeroCero);

        }
        for (int recore = 0; recore < s; recore = recore + 1) {
            unos.add(Unoscam.remove(recore));
            ceros.add(Ceroscam.remove(recore));
        }

        return this.FuncionObjetivo();
    }

    public double VNS(int semilla) {
        this.LimpiarSolucion();
        Random rnd = new Random(semilla);
        double maximo = this.SolucionAleatoria(rnd);
        ArrayList<Integer> UnosActual = (ArrayList<Integer>) unos.clone();
        ArrayList<Integer> CerosActual = (ArrayList<Integer>) ceros.clone();
        int kmax = 5;
        int s = 0;
        int k = 1;
        int bl = 0;

        //2
        while (bl < 25) {
            if (k > kmax) {
                k = 1;
            }
            //3
            if (k == 1) {
                s = (int) 0.02 * m;
            }
            if (k == 2) {
                s = (int) 0.04 * m;
            }
            if (k == 3) {
                s = (int) 0.06 * m;
            }
            if (k == 4) {
                s = (int) 0.08 * m;
            }
            if (k == 5) {
                s = (int) 0.1 * m;
            }

            this.GenerarVecino(s, rnd);
            //4
            this.BL(false, 0);
            bl = bl + 1;
            //5
            if (MAX > maximo) {
                UnosActual = (ArrayList<Integer>) unos;
                CerosActual = (ArrayList<Integer>) ceros;
                maximo = MAX;
                k = 1;
            } else {
                k = k + 1;
            }
        }

        unos = (ArrayList<Integer>) UnosActual;
        ceros = (ArrayList<Integer>) CerosActual;
        this.FuncionObjetivo();
        return MAX;

    }

    public static void main(String[] args) {
        MDP g = new MDP();
        g.acciones();
    }
}


import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import static java.lang.System.out;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Carlos Basso
 */
public class MDP {

    /**
     * @param args the command line arguments
     */
    double diversidad[][];
    int n;
    int m;
    Solucion Mejor = new Solucion();
    double DivMax = 0;

    public class Solucion {

        double MAX = 0;
        double MedDis = 0;
        ArrayList<Integer> sol = new ArrayList<Integer>();
        ArrayList<Integer> unos = new ArrayList<Integer>();
        ArrayList<Integer> ceros = new ArrayList<Integer>();

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

        public Solucion copia() {
            Solucion obj = new Solucion();
            obj.ceros = (ArrayList) this.ceros.clone();
            obj.MAX = this.MAX;
            obj.unos = (ArrayList) this.unos.clone();
            obj.sol = (ArrayList) this.sol.clone();
            return obj;
        }

        public int ValidarSolucion(Random a) {
            int salida;
            if (unos.isEmpty() || ceros.isEmpty()) {
                System.err.print("esta mal en la validacion");
                System.exit(0);
                salida = -1;
            } else if (unos.size() != m || ceros.size() != (n - m)) {
                int diferencia = unos.size() - m;
                if (diferencia > 0) {
                    while (ceros.size() != (n - m) && unos.size() != (m)) {
                        Collections.shuffle(unos, a);
                        int uno;
                        int pos = a.nextInt(unos.size());
                        uno = unos.remove(pos);
                        ceros.add(uno);
                    }
                } else {
                    while (ceros.size() != (n - m) && unos.size() != (m)) {
                        Collections.shuffle(ceros, a);
                        int cero;
                        int pos = a.nextInt(ceros.size());
                        cero = ceros.remove(pos);
                        unos.add(cero);
                    }
                }
                if (unos.size() == m && ceros.size() == (n - m)) {
                    salida = 1;
                } else {
                    salida = -1;
                }
            } else {
                salida = 0;
            }

            return salida;

        }

        public boolean RecomponerListas() {
            unos.clear();
            ceros.clear();
            for (int i = 0; i < sol.size(); i++) {
                if (sol.get(i) == 1) {
                    unos.add(i);
                } else {
                    ceros.add(i);
                }
            }
            return (unos.size() == m);
        }

        public double SolucionAleatoria(Random a) {
            LimpiarSolucion();
            for (int i = 0; i < m; i = i + 1) {
                int numero = a.nextInt(ceros.size());
                int cero = ceros.remove(numero);
                unos.add(cero);
                sol.set(cero, 1);
            }
            this.FuncionObjetivo();
            return MAX;
        }

        public Solucion SolDiversa(Random a, ArrayList<Integer> Ci, int TamP, ArrayList<Integer> Px) {
            LimpiarSolucion();

            int x = 0, i = 0;
            while (x < m) {
                int numero = a.nextInt(ceros.size());
//                System.out.println("entra"+Ci.get(numero));
//                System.out.println("entra: "+(1 - (double)((Ci.get(numero) /(double) TamP))));

                if ((1 - ((Ci.get(numero) / TamP))) > a.nextDouble()) {
                    //System.out.println("   entro");
                    //System.out.println("entra"+x);
                    int cero = ceros.remove(numero);
                    unos.add(cero);
                    sol.set(cero, 1);
                    x++;
                    x = x % n;
                }
                i++;
            }
            this.FuncionObjetivo();
            this.BL(false, true, 0, Px, 1000);
            return this.copia();
        }

        public double Gredy() {
            this.LimpiarSolucion();
            int masdiverso;
            masdiverso = this.BuscarMAsDiverso();
            unos.add(masdiverso);

            this.sol.set(masdiverso, 1);
            ceros.remove(masdiverso);
            for (int i = 1; i < m; i = i + 1) {
                int aux = this.Buscar();
                int cero = ceros.remove(aux);
                unos.add(cero);
                sol.set(cero, 1);
            }
            this.FuncionObjetivo();
            return this.MAX;
        }

        public double FuncionObjetivo() {
            double c = 0;

            if (unos.size() > m) {
                System.out.println(" error mas elementos: " + unos.size());
            }
            if (unos.size() < m) {
                System.out.println("error menos elementos" + unos.size());
            }
            for (int i = 0; i < unos.size(); i = i + 1) {
                for (int s = i; s < unos.size(); s = s + 1) {
                    if (unos.get(i) != unos.get(s)) {
                        if (unos.get(i) < unos.get(s)) {
                            c = c + diversidad[unos.get(i)][unos.get(s)];
                        } else {
                            c = c + diversidad[unos.get(s)][unos.get(i)];
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
                    MAX = MAX - diversidad[eliminado][unos.get(i)];
                }
                MAX = MAX + diversidad[nuevo][unos.get(i)];
            }/*
             for (int i = 0; i < m; i++) {
             MAX = MAX + Matriz[nuevo][unos.get(i)];
             }*/
            return MAX;
        }

        public int BuscarMAsDiverso() {
            double max = 0;
            int sal = 0;
            for (int i = 0; i < n; i = i + 1) {
                double cont = 0;
                for (int s = 0; s < n; s = s + 1) {
                    if (i < s) {
                        cont = diversidad[i][s] + cont;
                    }
                    if (i > s) {
                        cont = diversidad[s][i] + cont;
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
                        aux = diversidad[ceros.get(i)][unos.get(s)] + aux;
                    }
                    if (ceros.get(i) > unos.get(s)) {
                        aux = diversidad[unos.get(s)][ceros.get(i)] + aux;
                    }
                }
                if (max < aux) {
                    pos = i;
                    max = aux;
                }
            }
            return pos;
        }

        double Mutacion(int s, Random r) {
            ArrayList<Integer> Unoscam = new ArrayList<Integer>();
            ArrayList<Integer> Ceroscam = new ArrayList<Integer>();
//            for (int recore = 0; recore < s; recore = recore + 1) {
//                int numeroUno = r.nextInt(unos.size());
//                int numeroCero = r.nextInt(ceros.size());
//                Unoscam.add(numeroUno);
//                Ceroscam.add(numeroCero);
//                unos.remove(numeroUno);
//                ceros.remove(numeroCero);
//
//            }
            for (int recore = 0; recore < s; recore = recore + 1) {
//                int uno = Unoscam.remove(recore);
//                int cero = Ceroscam.remove(recore);
                Collections.shuffle(unos, r);
                Collections.shuffle(ceros, r);
                int uno = r.nextInt(unos.size());
                int cero = r.nextInt(ceros.size());
                int UNO = ceros.remove(cero);
                int CERO = unos.remove(uno);
//                int a = unos.remove(CERO);
//                int b = ceros.remove(UNO);
                ceros.add(CERO);
                unos.add(UNO);
                sol.set(CERO, 0);
                sol.set(UNO, 1);
            }

            return this.FuncionObjetivo();
        }

        public double BL(boolean Flag1, boolean Flag2, int semilla, ArrayList<Integer> x, int tamcota) {

            if (Flag1 == true) {
                Random rnd = new Random(semilla);
                this.LimpiarSolucion();
                this.SolucionAleatoria(rnd);
            }
            ArrayList<Integer> auxiliarCeros = (ArrayList<Integer>) ceros.clone();
            ArrayList<Integer> auxiliarunos = (ArrayList<Integer>) unos.clone();
            ArrayList<Integer> Soluciones = (ArrayList<Integer>) sol.clone();
            //empezamos en si la busqueda local
            this.FuncionObjetivo();
            boolean mejora = true;
            double max2 = this.MAX;
            int contador = 0;
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
                        sol.set(aux2, 1);
                        ceros.add(aux);
                        sol.set(aux, 0);
                        this.recalcula_diversidad(aux, aux2);
                        contador++;
                        x.set(0, x.get(0) + 1);


                        if (this.MAX > max2) {
                            mejora = true;
                            auxiliarunos = (ArrayList<Integer>) this.unos.clone();
                            auxiliarCeros = (ArrayList<Integer>) this.ceros.clone();
                            Soluciones = (ArrayList<Integer>) this.sol.clone();
                            max2 = this.MAX;


                        } else {
                            unos = (ArrayList<Integer>) auxiliarunos.clone();
                            ceros = (ArrayList<Integer>) auxiliarCeros.clone();
                            sol = (ArrayList<Integer>) Soluciones.clone();
                            this.MAX = MAX2;
                        }

                        if (Flag2 && contador > tamcota) {
                            //System.out.println("Entra: "+contador+"cota: "+tamcota);
                            this.unos = (ArrayList<Integer>) auxiliarunos.clone();
                            this.ceros = (ArrayList<Integer>) auxiliarCeros.clone();
                            this.sol = (ArrayList<Integer>) Soluciones.clone();
                            this.FuncionObjetivo();
                            return this.MAX;
                        }
                    }
                }
            }
            this.unos = (ArrayList<Integer>) auxiliarunos.clone();
            this.ceros = (ArrayList<Integer>) auxiliarCeros.clone();
            this.sol = (ArrayList<Integer>) Soluciones.clone();
            this.FuncionObjetivo();
            //System.out.print(sol.size());
            return this.MAX;

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

        public double GredyND(Random a, int l) {
            this.LimpiarSolucion();
            int siguiente = a.nextInt(ceros.size());
            unos.add(siguiente);
            ceros.remove(siguiente);
            for (int i = 1; i < m; i = i + 1) {
                siguiente = this.BuscarProbablidad(l, a);
                int cero = ceros.remove(siguiente);
                sol.set(cero, 1);
                unos.add(cero);
            }
            this.FuncionObjetivo();
            return this.MAX;
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
                            aux = diversidad[AuxCeros.get(i)][AuxUnos.get(s)] + aux;
                        }
                        if (AuxCeros.get(i) > AuxUnos.get(s)) {
                            aux = diversidad[AuxUnos.get(s)][AuxCeros.get(i)] + aux;
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
    }

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
        diversidad = new double[n][n];
        while (str.charAt(j) == ' ' || str.charAt(j) == '\n') {
            j++;
        }
        int k = j;
        for (int pos = 0; pos < n; pos++) {
            diversidad[pos][pos] = 0;
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
                diversidad[pos1][pos2] = Double.parseDouble(aux_str.substring(0, i));
                diversidad[pos2][pos1] = Double.parseDouble(aux_str.substring(0, i));
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
            long time_start, time_end;

            if (ALG == 0) {
                System.out.print("Algoritmo Genetico Generacional \n");
                cadena = "Algoritmo Genetico Generacional \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    double media = 0;
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.AG_Generacional(30, numero, 2, 0.1, 0.7, 0.1);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) + "\n";
                        System.out.print("\n\tMAXIMO: ");
                        media = media + Mejor.MAX;
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start));
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                    System.out.println("\nMEDIA:" + media / 10);

                }
                PrintStream scribirDD = new PrintStream("ALG_Genetico_Generacional.txt");
                scribirDD.append("Algoritmo Genetico Generacional:\n");
                scribirDD.append(cadena);
            }

            if (ALG == 1) {
                System.out.print("ALG genetico Estacionario \n");
                cadena = "ALG genetico Estacionario \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cadena = cadena + "\tfichero " + Ficheros.get(i) + " :\n";
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.AG_Estacionario(30, numero, 2, 0.1, 0.1);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start));
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }

                }
                PrintStream scribirDD = new PrintStream("ALG genetico Estacionario.txt");
                scribirDD.append("ALG genetico Estacionario:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 2) {
                System.out.print("ALG Memetico(1,2) \n");
                cadena = "ALG Memetico(1,2)  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 1, 2, false, 0);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(1,2).txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 3) {
                System.out.print("ALG Memetico(1,10) \n");
                cadena = "ALG Memetico  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 1, 10, false, 0);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start));
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(1,10).txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 4) {
                System.out.print("ALG Memetico(10,2) \n");
                cadena = "ALG Memetico  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 10, 2, false, 0);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(10,2).txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 5) {
                System.out.print("ALG Memetico(10,10) \n");
                cadena = "ALG Memetico  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 10, 10, false, 0);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(10,10).txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }

            if (ALG == 6) {
                System.out.print("ALG Memetico(1,2)Lim \n");
                cadena = "ALG Memetico(1,2)  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 1, 2, true, 1000);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(1,2)Lim.txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 7) {
                System.out.print("ALG Memetico(1,10)Lim \n");
                cadena = "ALG Memetico  \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 1, 10, true, 1000);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(1,10)Lim.txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 8) {
                System.out.print("ALG Memetico(10,2) Lim\n");
                cadena = "ALG Memetico Lim \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 10, 2, true, 1000);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(10,2)Lim.txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 9) {
                System.out.print("ALG Memetico(10,10)Lim \n");
                cadena = "ALG Memetico Lim \n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    this.leer_fichero_datos(Ficheros.get(i));
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 10, 10, true, 700);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";

                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                }

                PrintStream scribirDD = new PrintStream("ALG_Memetico(10,10)Lim.txt");
                scribirDD.append("ALG_Memetico:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 10) {
                double cont = 0;
                System.out.print("ALG Busqueda Dispersa \n");
                cadena = "ALG Busqueda Dispersa\n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cont = 0;
                    this.leer_fichero_datos(Ficheros.get(i));
                    cadena = cadena + "\t" + Ficheros.get(i) + "\n";
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.ALG_Memetico(10, numero, 2, 0.7, 0.1, 0.1, 10, 10, true, 700);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";
                        cont = cont + Mejor.MAX;
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(this.Mejor.MAX);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + this.Mejor.MAX + "\n";
                    }
                    System.out.println("Media: " + cont / Semilla);
                }

                PrintStream scribirDD = new PrintStream("Busqueda Dispersa.txt");
                scribirDD.append("ALG_Busqueda Dispersa:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 11) {
                double cont = 0;
                System.out.print("ALG BL \n");
                cadena = "ALG BL\n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cont = 0;
                    this.leer_fichero_datos(Ficheros.get(i));
                    cadena = cadena + "\t" + Ficheros.get(i) + "\n";
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.BusLo(1000, numero);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";
                        cont = cont + a;
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(a);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + a + "\n";
                    }
                    System.out.println("Media: " + cont / Semilla);
                }

                PrintStream scribirDD = new PrintStream("BL.txt");
                scribirDD.append("ALG_BL:\n");
                scribirDD.append(cadena);
            }
            if (ALG == 12) {
                double cont = 0;
                System.out.print("ALG BL LIM \n");
                cadena = "ALG BL LIM\n";
                for (int i = 0; i < Ficheros.size(); i = i + 1) {
                    cont = 0;
                    this.leer_fichero_datos(Ficheros.get(i));
                    cadena = cadena + "\t" + Ficheros.get(i) + "\n";
                    for (int j = 0; j < Semilla; j = j + 1) {
                        int numero = Semillas.get(j);
                        cadena = cadena + "\n\t\t Semilla: " + numero;
                        time_start = System.currentTimeMillis();
                        double a = this.BusLo(1000, numero);
                        time_end = System.currentTimeMillis();
                        cadena = cadena + "\t\tTiempo: " + (time_end - time_start) / 1000 + " s" + "\n";
                        cont = cont + a;
                        System.out.print("\n\tMAXIMO: ");
                        System.out.print(a);
                        System.out.print("\tTiempo: " + (time_end - time_start) / 1000 + " s");
                        cadena = cadena + "\t\t\tMaximo: " + a + "\n";
                    }
                    System.out.println("Media: " + cont / Semilla);
                }

                PrintStream scribirDD = new PrintStream("BL_LIM.txt");
                scribirDD.append("ALG_BL LIM:\n");
                scribirDD.append(cadena);
            }
        } catch (IOException ex) {
            System.out.println("\nNo se ha encontrado el archivo de texto CON LAS ACCIONES PARA EL PROBLEMA.");
        }
    }
    /*
     * Algoritmos Geneticos
     * Este deveria cambiar completamente la poblacioninicial
     */

    public double AG_Generacional(int TamPoblacion, int semilla, int TamTorneo, double mutar, double cruzar, double Tammutar) {
        ArrayList<Solucion> poblacion = new ArrayList();
        Solucion aux;
        Random a = new Random();
        a.setSeed(semilla);

        Mejor = new Solucion();
        /*
         * Generacion de la Poblacion inicial
         */
        for (int i = 0; i < TamPoblacion; i++) {
            aux = new Solucion();
            aux.SolucionAleatoria(a);
            // aux.GredyND(a, 4);
            //aux.Gredy();
            poblacion.add(aux);
        }
//        for (int o = 0; o < poblacion.size(); o++) {
//            System.out.println(poblacion.get(o).MAX);
//        }
        Collections.sort(poblacion, new c());
        Mejor = poblacion.get(0);
        ArrayList<Integer> x = new ArrayList();
        x.add(0);
        while (x.get(0) < 100000) {
            //System.out.println("x es : " + x);
//            if (poblacion.size() > 30) {
//                System.exit(0);
//            }
            ArrayList<Solucion> poblacionAux1 = new ArrayList();
            for (int Torneos = 0; Torneos < TamPoblacion / 2; Torneos++) {
                Collections.shuffle(poblacion, a);
                /*
                 * Seleccion de Padres
                 */
                poblacionAux1.clear();
                /*
                 * Torneo
                 */
                ArrayList<Integer> numeros = Torneo(poblacion.size(), a, TamTorneo);

                if (poblacion.get(numeros.get(0)).MAX > poblacion.get(numeros.get(1)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(0)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(1)));
                }
                if (poblacion.get(numeros.get(2)).MAX > poblacion.get(numeros.get(3)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(2)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(3)));
                }
                Solucion Sec1 = poblacionAux1.get(0);
                Solucion Sec2 = poblacionAux1.get(1);
//                if(Sec1.MAX<Sec2.MAX){
//                    System.out.print("liaooooo");
//                }
                poblacion.remove(Sec1);
                poblacion.remove(Sec2);

                ArrayList<Solucion> hijos = new ArrayList();
                if (a.nextDouble() < cruzar) {
                    //System.out.println(x);
                    hijos = Cruce(Sec1, Sec2, a, mutar, Tammutar, x);
                    x.set(0, x.get(0) + 1);
                    x.set(0, x.get(0) + 1);
                } else {
                    if (a.nextFloat() < mutar) {
                        Sec2.Mutacion((int) (0.1 * m), a);
                        hijos.add(Sec2);
                        x.set(0, x.get(0) + 1);
                    } else {
                        hijos.add(Sec2);
                    }
                    if (a.nextFloat() < mutar) {
                        Collections.sort(Sec1.unos);
                        //System.out.println(Sec1.unos);
                        Sec1.Mutacion((int) (0.1 * m), a);
                        Collections.sort(Sec1.unos);
                        //System.out.println(Sec1.unos);
                        hijos.add(Sec1);
                        x.set(0, x.get(0) + 1);
                    } else {
                        hijos.add(Sec1);
                    }

                }
                /*
                 * Remplazo
                 */


                // System.out.println("Añadidos: " + poblacion.size());
                /*
                 * Miramos si es mejor el resultado
                 */

                if (hijos.get(0).MAX > Mejor.MAX) {
                    Mejor = hijos.get(0).copia();
                }
                if (hijos.get(1).MAX > Mejor.MAX) {
                    Mejor = hijos.get(1).copia();
                }
                poblacion.add(hijos.get(0).copia());
                poblacion.add(hijos.get(1).copia());
                hijos.clear();
                Collections.sort(poblacion, new c());
                if (poblacion.get(poblacion.size() - 1).MAX < Mejor.MAX && iguales(poblacion, Mejor) != false) {
                    poblacion.set(poblacion.size() - 1, Mejor.copia());
                    //System.out.print("entra");
                }
            }// Fin del For

            Collections.sort(poblacion, new c());
//            if (Mejor.MAX < poblacion.get(0).MAX) {
////                Mejor = (MDP.Solucion) poblacion.get(0).copia();
////
////            }
//            if (poblacion.get(poblacion.size() - 1).MAX < Mejor.MAX && iguales(poblacion, Mejor) != false) {
//                poblacion.set(poblacion.size() - 1, Mejor.copia());
//                System.out.print("entra");
//            }

        }//Fin del while
        //Collections.sort(poblacion, new c());
//        System.out.print("\nFinal\n");
//        for (int o = 0; o < poblacion.size(); o++) {
//            System.out.println(poblacion.get(o).MAX);
//        }
        return Mejor.MAX;
    }
    /*
     * CAmbiar de geeraciona a estacionario
     */

    public double AG_Estacionario(int TamPoblacion, int semilla, int TamTorneo, double mutar, double Tammutar) {
        ArrayList<Solucion> poblacion = new ArrayList();
        Solucion aux;
        Random a = new Random(semilla);
        Mejor = new Solucion();
        /*
         * Generacion de la Poblacion inicial
         */

        for (int i = 0; i < TamPoblacion; i++) {
            aux = new Solucion();
            aux.SolucionAleatoria(a);
            poblacion.add(aux);
        }
        ArrayList<Integer> x = new ArrayList();
        x.add(0);
        while (x.get(0) < 100000) {

            Collections.sort(poblacion, new c());
            ArrayList<Solucion> poblacionAux1 = new ArrayList();

            for (int Torneos = 0; Torneos < 2; Torneos++) {
                /*
                 * Seleccion de Padres
                 */
                poblacionAux1.clear();
                ArrayList<Integer> numeros = Torneo(poblacion.size(), a, TamTorneo);

                if (poblacion.get(numeros.get(0)).MAX > poblacion.get(numeros.get(1)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(0)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(1)));
                }
                if (poblacion.get(numeros.get(2)).MAX > poblacion.get(numeros.get(3)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(2)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(3)));
                }
                Solucion Sec1 = poblacionAux1.get(0);
                Solucion Sec2 = poblacionAux1.get(1);
                poblacion.remove(Sec1);
                poblacion.remove(Sec2);
                /*
                 * Cruce
                 */
                ArrayList<Solucion> hijos;
                //System.out.println("antes" + x);
                hijos = Cruce(Sec1, Sec2, a, mutar, Tammutar, x);
                //System.out.println("despues" + x);
                x.set(0, x.get(0) + 1);
                x.set(0, x.get(0) + 1);
                /*
                 * Remplazo
                 */
                hijos.add(Sec1);
                hijos.add(Sec2);
                //     System.out.print(hijos);
                Collections.sort(hijos, new c());
                poblacion.add(hijos.get(0));
                poblacion.add(hijos.get(1));
                Collections.sort(poblacion, new c());
                poblacion.set(poblacion.size() - 1, hijos.get(2));
                poblacion.set(poblacion.size() - 2, hijos.get(3));
                Collections.shuffle(poblacion);


            }

////            Collections.sort(poblacion, new c());
////            if (Mejor.MAX < poblacion.get(0).MAX ) {
////                Mejor = (MDP.Solucion) poblacion.get(0);
////            }
        }
        Collections.sort(poblacion, new c());
//        for (int o = 0; o < poblacion.size(); o++) {
//            System.out.println(poblacion.get(o).MAX);
//        }

        Mejor = (MDP.Solucion) poblacion.get(0);
        return Mejor.MAX;
    }

    public double ALG_Memetico(int TamPoblacion, int semilla, int TamTorneo, double cruzar, double mutar, double Tammutar, int periodo, int aplica, boolean cota, int tamcota) {
        ArrayList<Solucion> poblacion = new ArrayList();
        Solucion aux;
        Random a = new Random();
        a.setSeed(semilla);
        Mejor = new Solucion();
        /*
         * Generacion de la Poblacion inicial
         */
        for (int i = 0; i < TamPoblacion; i++) {
            aux = new Solucion();
            aux.SolucionAleatoria(a);
            poblacion.add(aux.copia());
        }
        //Collections.sort(poblacion, new c());
        Mejor = poblacion.get(0).copia();
        ArrayList<Integer> x = new ArrayList();
        x.add(0);
        int con = 0;
        while (x.get(0) < 100000) {
            con++;
            //System.out.println(x.get(0));
            ArrayList<Solucion> poblacionAux1 = new ArrayList();
            for (int Torneos = 0; Torneos < TamPoblacion / 2; Torneos++) {

                //Collections.shuffle(poblacion, a);
                /*
                 * Seleccion de Padres(Torneo)
                 */
                poblacionAux1.clear();
                ArrayList<Integer> numeros = Torneo(poblacion.size(), a, TamTorneo);
                if (poblacion.get(numeros.get(0)).MAX > poblacion.get(numeros.get(1)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(0)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(1)));
                }
                if (poblacion.get(numeros.get(2)).MAX > poblacion.get(numeros.get(3)).MAX) {
                    poblacionAux1.add(poblacion.get(numeros.get(2)));
                } else {
                    poblacionAux1.add(poblacion.get(numeros.get(3)));
                }
                Solucion Sec1 = poblacionAux1.get(0).copia();
                Solucion Sec2 = poblacionAux1.get(1).copia();

                poblacion.remove(Sec1);
                poblacion.remove(Sec2);

                ArrayList<Solucion> hijos = new ArrayList();
                if (a.nextDouble() < cruzar) {
                    hijos = Cruce(Sec1, Sec2, a, mutar, Tammutar, x);
                    x.set(0, x.get(0) + 1);
                    x.set(0, x.get(0) + 1);
                } else {
                    if (a.nextFloat() < mutar) {
                        Sec2.Mutacion((int) (0.1 * m), a);
                        hijos.add(Sec2);
                        x.set(0, x.get(0) + 1);
                    } else {
                        hijos.add(Sec2);
                    }
                    if (a.nextFloat() < mutar) {
                        Collections.sort(Sec1.unos);
                        Sec1.Mutacion((int) (0.1 * m), a);
                        hijos.add(Sec1);
                        x.set(0, x.get(0) + 1);
                    } else {
                        hijos.add(Sec1);
                    }

                }
                /*
                 * Remplazo
                 */

                /*
                 * Miramos si es mejor el resultado
                 */

                if (hijos.get(0).MAX > Mejor.MAX) {
                    Mejor = hijos.get(0).copia();
                }
                if (hijos.get(1).MAX > Mejor.MAX) {
                    Mejor = hijos.get(1).copia();
                }
                poblacion.add(hijos.get(0).copia());
                poblacion.add(hijos.get(1).copia());
                hijos.clear();
                Collections.sort(poblacion, new c());
                if (poblacion.get(poblacion.size() - 1).MAX < Mejor.MAX && iguales(poblacion, Mejor) != false) {
                    poblacion.set(poblacion.size() - 1, Mejor.copia());
                    //System.out.print("entra");
                }
            }// Fin del For
            if (con % periodo == 0) {
                Collections.sort(poblacion, new c());
                for (int y = 0; y < aplica; y++) {
                    poblacion.get(y).BL(false, cota, 0, x, tamcota);
                }
            }
            if (poblacion.get(0).MAX > Mejor.MAX) {
                Mejor = poblacion.get(0).copia();
            }
            if (poblacion.get(1).MAX > Mejor.MAX) {
                Mejor = poblacion.get(1).copia();
            }

        }//Fin del while
        Collections.sort(poblacion, new c());

        return Mejor.MAX;

    }

    public double ALG_BD(int P, int semilla, int R1, int R2) {
        ArrayList<Solucion> r1 = new ArrayList();
        ArrayList<Solucion> r2 = new ArrayList();
        ArrayList<Solucion> p = new ArrayList();
        ArrayList<Solucion> p2 = new ArrayList();
        ArrayList<Integer> Ci = new ArrayList();
        for (int i = 0; i < n; i = i + 1) {
            Ci.add(0);
        }
        Solucion aux;
        Random a = new Random();
        a.setSeed(semilla);
        Mejor = new Solucion();
        /*
         * Generacion de la Poblacion inicial
         */
        aux = new Solucion();
        aux.SolucionAleatoria(a);
        p.add(aux.copia());
        actualizarCi(Ci, aux);
        ArrayList<Integer> x = new ArrayList();
        x.add(0);
        for (int i = 0; i < P; i++) {
            //System.err.println(Ci);
            aux = new Solucion();
            p.add(aux.SolDiversa(a, Ci, p.size(), x));
            actualizarCi(Ci, aux);
        }
        p = p2;
        x.set(0, 0);
        int con = 0;
        while (x.get(0) < 100000) {
            //Collections.sort(poblacion, new c());
            //poner

            for (int i = 0; i < P; i++) {
                this.MedDist(p.get(0), p);
            }
            Collections.sort(p, new c());
            if (Mejor.MAX < p.get(0).MAX) {
                Mejor = p.get(0).copia();
            }
            r1.add(p.get(0).copia());
            r1.add(p.get(1).copia());
            r1.add(p.get(2).copia());
            Collections.sort(p2, new b());
            r2.add(p2.get(0).copia());
            r2.add(p2.get(1).copia());
            r2.add(p2.get(2).copia());
            p.clear();
            p2.clear();

            /*
             * Cruces
             */
            ArrayList<Solucion> Hijos1 = new ArrayList();
            ArrayList<Solucion> Hijos2 = new ArrayList();
            Hijos1 = Cruce(r1.get(0), r1.get(1), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));
            Hijos1 = Cruce(r1.get(0), r1.get(1), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));

            Hijos1 = Cruce(r1.get(0), r1.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));
            Hijos1 = Cruce(r1.get(0), r1.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));

            Hijos1 = Cruce(r1.get(1), r1.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));
            Hijos1 = Cruce(r1.get(1), r1.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));


            Hijos1 = Cruce(r2.get(0), r2.get(1), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));

            Hijos1 = Cruce(r2.get(0), r2.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));

            Hijos1 = Cruce(r2.get(1), r2.get(2), a, 0, 0, x);
            Hijos2.add(Hijos1.get(0));
            Hijos2.add(Hijos1.get(1));

            for (int cru = 0; cru < 3; cru++) {
                for (int cru2 = 0; cru2 < 3; cru2++) {
                    Hijos1 = Cruce(r2.get(cru), r1.get(cru2), a, 0, 0, x);
                    Hijos2.add(Hijos1.get(0));
                    Hijos2.add(Hijos1.get(1));
                }
            }
            //System.out.println(Hijos2.size());
            for (int s = 0; s < Hijos2.size(); s++) {
                p2.add(Hijos2.get(s).copia());
            }
            for (int c = 0; c < 3; c++) {
                Hijos2.get(c).BL(false, true, 0, x, 1000);
            }
            p = Hijos2;

        }
        Collections.sort(p, new c());
        if (Mejor.MAX < p.get(0).MAX) {
            Mejor = p.get(0).copia();
        }
        // System.out.println(Mejor.MAX);
        return Mejor.MAX;
    }
    /*
     * Operadores Para Algoritmos 
     */

    public void pruebas() {

        ALG_BD(30, 123456789, 3, 3);

    }

    public double BusLo(int limite, int semilla) {
        Solucion a = new Solucion();
        ArrayList<Integer> x = new ArrayList();
        x.add(0);
        if (limite == 0) {
            a.BL(true, false, semilla, x, 0);
        } else {
            a.BL(true, true, semilla, x, limite);
        }
        return a.MAX;

    }

    public void actualizarCi(ArrayList<Integer> Ci, Solucion S) {
        for (int i = 0; i < S.unos.size(); i++) {
            int num = S.unos.get(i);
            Ci.set(num, Ci.get(num) + 1);
        }
    }

    public boolean igual(Solucion a, Solucion b) {
//        if (a.sol == b.sol) {
//            System.out.print("cierto");
//        }
//        if (a.ceros == b.ceros) {
//            System.out.print("cierto2");
//        }
        for (int i = 0; i < n; i++) {
            if (a.sol.get(i) != b.sol.get(i)) {
                return false;
            }
            if (a.ceros.size() > i) {
                if (a.ceros.indexOf(b.ceros.get(i)) == -1) {
                    return false;
                }
            }
            if (a.unos.size() > i) {
                if (a.unos.indexOf(b.unos.get(i)) == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean iguales(ArrayList<Solucion> X, Solucion Y) {
        for (int i = 0; i < X.size(); i++) {
            if (igual(X.get(i), Y)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Solucion> Cruce(Solucion p1, Solucion p2, Random a, double mutar, double Tammutacion, ArrayList<Integer> x) {
        ArrayList<Solucion> s = new ArrayList<Solucion>();
        int punto1 = a.nextInt(p1.sol.size());
        // int punto2 = a.nextInt(p1.sol.size());
        int punto2 = a.nextInt(p1.sol.size() - punto1);
        punto2 = punto2 + punto1;
        if (punto2 - punto1 < (int) (n / 12)) {
            punto2 = punto2 + (((n - punto2) * 2) / 3);
        }


        Solucion s1 = new Solucion(), s2 = new Solucion();
        s1.LimpiarSolucion();
        s2.LimpiarSolucion();
        for (int i = 0; i < p1.sol.size(); i++) {

            if (i < punto1) {
                s1.sol.set(i, p2.sol.get(i));
                s2.sol.set(i, p1.sol.get(i));
            } else if (i > punto1 && i < punto2) {
                s1.sol.set(i, p1.sol.get(i));
                s2.sol.set(i, p2.sol.get(i));
            } else {
                s1.sol.set(i, p2.sol.get(i));
                s2.sol.set(i, p1.sol.get(i));
            }
        }

        s2.RecomponerListas();
        s1.RecomponerListas();
        if (s1.ValidarSolucion(a) != -1) {
            if (mutar > a.nextDouble()) {
                s1.Mutacion((int) (0.1 * m), a);
                x.set(0, x.get(0) + 1);
            }
            s1.FuncionObjetivo();
        } else {
            System.err.print("error al validar");
        }

        if (s2.ValidarSolucion(a) != -1) {
            if (mutar > a.nextDouble()) {
                s2.Mutacion((int) (0.1 * m), a);
                x.set(0, x.get(0) + 1);
            }
            s2.FuncionObjetivo();
        } else {
            System.err.print("error al validar");
        }
        s.add(s1);
        s.add(s2);

        return s;
    }

    public ArrayList<Integer> Torneo(int TamPoblacion, Random a, int TamTorneo) {
        ArrayList<Integer> Ganadores = new ArrayList();

        Ganadores.add(a.nextInt(TamPoblacion));
        int torneo = a.nextInt(TamPoblacion);
        for (int i = 1; i < TamTorneo * 2; i++) {
            while (-1 != Ganadores.indexOf(torneo)) {
                torneo = a.nextInt(TamPoblacion);
            }
            Ganadores.add(torneo);
        }
        //System.out.print(Ganadores);
        return Ganadores;
    }

    protected double distancia(Solucion s1, Solucion s2) {
        double d = 0;
        for (int i = 0; i < s1.sol.size(); i++) {
            if (s1.sol.get(i) != s2.sol.get(i)) {
                d++;
            }
        }
        return d;

    }

    public double MedDist(Solucion s1, ArrayList<Solucion> Pob) {
        double dist = 0;
        for (int i = 0; i < Pob.size(); i++) {
            dist = dist + distancia(s1, Pob.get(i));
        }
        s1.MedDis = dist / Pob.size();
        return dist / Pob.size();
    }
    /*
     * Ordenar
     */

    public class c implements Comparator {

        @Override
        public int compare(Object arg0, Object arg1) {
            MDP.Solucion x = (MDP.Solucion) arg0;
            MDP.Solucion y = (MDP.Solucion) arg1;
            if (x.MAX > y.MAX) {
                return -1;
            } else if (x.MAX < y.MAX) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public class b implements Comparator {

        @Override
        public int compare(Object arg0, Object arg1) {
            MDP.Solucion x = (MDP.Solucion) arg0;
            MDP.Solucion y = (MDP.Solucion) arg1;
            if (x.MedDis > y.MedDis) {
                return -1;
            } else if (x.MedDis < y.MedDis) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        MDP g = new MDP();
        g.acciones();
    }
}

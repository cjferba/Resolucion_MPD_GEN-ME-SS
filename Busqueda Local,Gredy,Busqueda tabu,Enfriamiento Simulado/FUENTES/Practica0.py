#~ #!/usr/bin/env python
#
#       Practica0.py
#       
#       Copyright 2012 Carlos Jesus Fernandez Basso <cjferba@gmail.com>
#       
#       This p rogram is free software; you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation; either version 2 of the License, or
#       (at your option) any later version.
#       
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#       
#       You should have received a copy of the GNU General Public License
#       along with this program; if not, write to the Free Software
#       Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#       MA 02110-1301, USA.
import random
from random import shuffle
import sys
import math
#~ MASK=2147483647
#~ PRIME=65539
#~ SCALE=0.4656612875e-9
#~ Seed=0
class datos:
	def __init__(self):
		self.ma={}
		self.sol=[]
		self.Lista1=[]
		self.Lista0=[]
		self.n=0
		self.m=0
		self.max=0
	def see(self):
		print self.n
		print self.m
		print self.ma
	def LecturaL1(self,texto):
		try:
			f= open(texto)       
		except IOError:                     
			print "El fichero no existe, ERROR"
			#~ exit("error en la gestion de ficheros")
		dato = f.readline()
		num=dato.split()
		self.n=int(num[0])
		self.m=int(num[1])
#		self.sol=[500]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		dato = f.readline()
		while(dato!=""):
			num=dato.split()
			self.ma[(int(num[0]),int(num[1]))]=float(num[2])
			dato = f.readline()
		f.close()

	def BuscarMasDiver(self):
		max=0;
		sal=0;
		for i in range(self.n):
			cont=0;
			for s in range(self.n):
				if(i<s):
					cont=self.ma[i,s]+cont
				if(i>s):
					cont=self.ma[s,i]+cont
			#~ print "elemento"+str(i)+"contador es:"+str(cont)
			if(max<cont):
				sal=i
				max=cont
		return sal

	def Buscar(self,ListaU,ListaC):
		max=0
		for i in range(0,len(ListaC)):
			aux=0
			for s in range(0 ,len(ListaU)):
				if(ListaC[i]<ListaU[s]):
					aux=self.ma[ListaC[i],ListaU[s]]+aux
				if(ListaC[i]>ListaU[s]):
					aux=self.ma[ListaU[s],ListaC[i]]+aux
			if(max<aux):
				pos=i
				max=aux
		return [pos,max]
	def FunBuena(self):
		solnum=[]
		c=0
		for i in range(len(self.Lista1)):
			for s in range(i,len(self.Lista1)):
				#~ if(i!=s):
				if(self.Lista1[i]!=self.Lista1[s]):
					if(self.Lista1[i]<self.Lista1[s]):
						c=c+self.ma[self.Lista1[i],self.Lista1[s]]
					else:
						c=c+self.ma[self.Lista1[s],self.Lista1[i]]
		self.max=c
	def Gredy (self):
		self.Lista1=[]
		self.Lista0=[]
		self.sol=[]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		#~ random.seed(18987)
		#~ numeroAle= int(random.random()*1000%500)
		numeroAle=self.BuscarMasDiver()
		#~ numeroAle=289
		#~ self.sol[numeroAle]=1
		self.Lista1.append(numeroAle)
		a=self.Lista0.index(numeroAle)
		self.Lista0.remove(a)
		for y in range(1, self.m):
			aux=self.Buscar(self.Lista1,self.Lista0)
			a=self.Lista0[aux[0]]
			#~ print str(a)+"\n"
			self.Lista1.insert(0,a)
			self.Lista0.remove(a)
		for y in range(len(self.Lista1)):
			self.sol[self.Lista1[y]]=1
		self.FunBuena()

	def BA(self,semilla):
		self.Lista1=[]
		self.Lista0=[]
		L0=[]
		Lsol=[]
		self.sol=[]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		Listanum=self.sol[:]
		Lsol=self.sol[:]
		ListaUnos=[]
		ListaCeros=self.Lista0[:]
		L0=self.Lista0[:]
		repetidos=[]
		random.seed(semilla)
		max=0;

		#~ for i in range(self.n):
			#~ Listanum.append(0)
		for i in range(100000):
			for s in range(self.m):
				numeroAle =random.randint(0, len(ListaCeros)-1)
				#~ numeroAle = int(random.random()*100000%self.n)
				#~ while(numeroAle in repetidos):
					#~ numeroAle =random.randint(0, self.n)
					#~ numeroAle = int(random.random()*100000%self.n)
				Listanum[ListaCeros[numeroAle]]=1
				ListaUnos.append(ListaCeros[numeroAle])
				ListaCeros.remove(ListaCeros[numeroAle])
				#~ a=ListaCeros.index(numeroAle)
				#~ print a
				#~ ListaCeros.remove(a)
				#~ repetidos.append(numeroAle)
			num=self.Diversidad(Listanum)
			if(max<num):
				max=num
				self.sol=Listanum[:]
				self.Lista1=ListaUnos[:]
				self.Lista0=ListaCeros[:]
				self.max=max
			Listanum=Lsol[:]
			repetidos=[]
			ListaCeros=L0[:]
			ListaUnos=[]
		self.FunBuena()



#~ Crear busqueda del elemento mas diverso
#~ CAmbiar sol por lista1 y lista0
	def Diversidad2(self,ListaUnos):
		solnum=[]
		c=0
		for i in range(len(ListaUnos)):
			for s in range(i,len(ListaUnos)):
				#~ if(i!=s):
				if(ListaUnos[i]!=ListaUnos[s]):
					if(ListaUnos[i]<ListaUnos[s]):
						c=c+self.ma[ListaUnos[i],ListaUnos[s]]
					else:
						c=c+self.ma[ListaUnos[s],ListaUnos[i]]
		return c





	def Diversidad(self,solucion):
		solnum=[]
		c=0
		for i in range(len(solucion)):
			if(solucion[i]==1):
				solnum.append(i)
		for i in range(len(solnum)):
			for s in range(i,len(solnum)):
				if(i!=s):
					c=c+self.ma[i,s]
		return(c)
	



	#~ def Gvecinos(self):
		#~ Listavecinos=[]

	def BL(self,semilla):
		self.Lista1=[]
		self.Lista0=[]
		self.sol=[]
		self.max=0
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		#inserat unos y ceros
		#se genera una solucion aleatoria
		random.seed(semilla)
		mejora=True;
		repetidos=[]
		for s in range(self.m):
				numeroAle =random.randint(0, self.n-1)
				#~ numeroAle = int(random.random()*100000%self.n)
				while(numeroAle in repetidos):
					numeroAle =random.randint(0, self.n-1)
					#~ numeroAle = int(random.random()*100000%self.n)
				self.sol[numeroAle]=1
				self.Lista1.append(numeroAle)
				repetidos.append(numeroAle)
				self.Lista0.remove(numeroAle)
		#empezamos en si la busqueda local
		self.FunBuena()
		print "sol: "+str(self.max)
		while(mejora==True):
			#print "entra"
			mejora=False
			shuffle(self.Lista1)
			ListaU=self.Lista1[:]
			ListaO=self.Lista0[:]
			ListaSol=self.sol[:]
			for i in range(len(self.Lista1)): #and mejora==False:
				for j in range(len(self.Lista0)):# and mejora==False:
					aux2=ListaO[j]
					ListaO.remove(ListaO[j])
					aux=ListaU[i]
					ListaU.remove(ListaU[i])
					ListaU.append(aux2)
					ListaO.append(aux)
					max2=self.Diversidad2(ListaU)
					if(self.max<max2):
						mejora=True
						self.Lista1=ListaU[:]
						self.Lista0=ListaO[:]
						self.sol=ListaSol[:]
						self.max=max2
						break
					else:
						ListaU=self.Lista1[:]
						ListaO=self.Lista0[:]
						ListaSol=self.sol[:]
				if(mejora==True):
					break
		#~ print self.max


	def BVND(self,semilla):
		self.Lista1=[]
		self.Lista0=[]
		self.sol=[]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		random.seed(semilla)
		mejora=True;
		repetidos=[]
		for s in range(self.m):
				numeroAle =random.randint(0, self.n-1)
				while(numeroAle in repetidos):
					numeroAle =random.randint(0, self.n-1)
					#~ numeroAle = int(random.random()*100000%self.n)
				self.sol[numeroAle]=1
				self.Lista1.append(numeroAle)
				repetidos.append(numeroAle)
				self.Lista0.remove(numeroAle)
		#empezamos en si la busqueda local
		self.FunBuena()
		print "sol inicial VND: "+str(self.max)
		k=1
		max=self.max
		N=0
		while(k<=3 and N<=100000):
			mejora=False
			#~ while(mejora==true)
			ListaU=self.Lista1[:]
			ListaO=self.Lista0[:]
			ListaSol=self.sol[:]
			#cuadramos cual es la vecindad
			#~ print str(self.max)+"k: "+str(k)
			if(k==1):
				Vecindad=int(0.2*self.m*(self.n-self.m))
				numeroAle1 =random.randint(0, len(ListaU)-1)
				for j in range(Vecindad):
					aux1=random.randint(0, len(ListaO)-1)
					Cero1=ListaO[aux1]
					Uno1=ListaU[numeroAle1]
					ListaU.append(Cero1)
					ListaO.remove(Cero1)
					ListaO.append(Uno1)
					ListaU.remove(Uno1)
					num=self.Diversidad2(ListaU)
					if(max<num):
						#~ print num
						mejora=True
						self.Lista0=ListaO[:]
						self.Lista1=ListaU[:]
						self.max=num
						max=num
					else:
						ListaU=self.Lista1[:]
						ListaO=self.Lista0[:]
				N=N+Vecindad
			if(k==2):
				Vecindad=int(0.5*self.m*(self.n-self.m))
				numeroAle1 =random.randint(0, len(ListaU)-1)
				numeroAle2 =random.randint(0, len(ListaU)-1)
				while(numeroAle1==numeroAle2):
					numeroAle1 =random.randint(0, len(ListaU)-1)
					numeroAle2 =random.randint(0, len(ListaU)-1)
				for j in range(Vecindad):
					aux1=random.randint(0, len(ListaO)-1)
					aux2=random.randint(0, len(ListaO)-1)
					while(aux1==aux2):
						aux1=random.randint(0, len(ListaO)-1)
						aux2=random.randint(0, len(ListaO)-1)
					Cero1=ListaO[aux1]
					Cero2=ListaO[aux2]
					Uno1=ListaU[numeroAle1]
					Uno2=ListaU[numeroAle2]
					ListaU.append(Cero1)
					ListaU.append(Cero2)
					ListaO.remove(Cero1)
					ListaO.remove(Cero2)
					ListaO.append(Uno1)
					ListaO.append(Uno2)
					ListaU.remove(Uno1)
					ListaU.remove(Uno2)
					num=self.Diversidad2(ListaU)
					if(max<num):
						mejora=True
						self.Lista0=ListaO[:]
						self.Lista1=ListaU[:]
						self.max=num
						max=num
					else:
						ListaU=self.Lista1[:]
						ListaO=self.Lista0[:]
				N=N+Vecindad
			if(k==3):
				Vecindad=int(self.m*(self.n-self.m))
				numeroAle1 =random.randint(0, len(ListaU)-1)
				numeroAle2 =random.randint(0, len(ListaU)-1)
				numeroAle3 =random.randint(0, len(ListaU)-1)
				while(numeroAle1==numeroAle2 or numeroAle1==numeroAle3 or numeroAle3==numeroAle2):
					numeroAle1 =random.randint(0, len(ListaU)-1)
					numeroAle2 =random.randint(0, len(ListaU)-1)
					numeroAle3 =random.randint(0, len(ListaU)-1)
				for j in range(Vecindad):
					aux1=random.randint(0, len(ListaO)-1)
					aux2=random.randint(0, len(ListaO)-1)
					aux3=random.randint(0, len(ListaO)-1)
					while(aux1==aux2 or aux1==aux3 or aux3==aux2):
						aux1=random.randint(0, len(ListaO)-1)
						aux2=random.randint(0, len(ListaO)-1)
						aux3=random.randint(0, len(ListaO)-1)
					Cero1=ListaO[aux1]
					Cero2=ListaO[aux2]
					Cero3=ListaO[aux3]
					Uno1=ListaU[numeroAle1]
					Uno2=ListaU[numeroAle2]
					Uno3=ListaU[numeroAle3]
					ListaU.remove(Uno1)
					ListaU.remove(Uno2)
					ListaU.remove(Uno3)
					ListaO.remove(Cero1)
					ListaO.remove(Cero2)
					ListaO.remove(Cero3)
					ListaU.append(Cero1)
					ListaU.append(Cero2)
					ListaU.append(Cero3)
					ListaO.append(Uno1)
					ListaO.append(Uno2)
					ListaO.append(Uno3)
					num=self.Diversidad2(ListaU)
					if(max<num):
						#~ print max
						#~ print "\n"
						mejora=True
						self.Lista0=ListaO[:]
						self.Lista1=ListaU[:]
						self.max=num
						max=num
					else:
						ListaU=self.Lista1[:]
						ListaO=self.Lista0[:]
				N=N+Vecindad
			if(mejora==False):
				k=k+1
				mejora=True
			

	def CalBmax(self):
		Bmax=0
		for i in range(self.n-2):
			for s in range(i+1,self.n-1):
				if(i<s):
					Bmax=self.ma[i,s]+Bmax
				if(i>s):
					Bmax=self.ma[s,i]+Bmax
		return Bmax
####BIEN#####
	def TS(self,semilla):
		self.Lista1=[]
		self.Lista0=[]
		self.sol=[]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		random.seed(semilla)
		Bmax=self.CalBmax()
		Tfinal=1/Bmax
		#~ Tfinal=0.000003
		for s in range(self.m):
				numeroAle =random.randint(0, self.n-1)
				while(numeroAle in self.Lista1):
					numeroAle =random.randint(0, self.n-1)
				self.sol[numeroAle]=1
				self.Lista1.append(numeroAle)
				#~ repetidos.append(numeroAle)
				self.Lista0.remove(numeroAle)
		self.FunBuena()
		print "solucion aleatoria"+str(self.max)
		coste=(1-self.max)/Bmax
		T=(0.3/(-math.log(0.3,10)))*(coste)
		B=(T-Tfinal)/(333*T*Tfinal)
		ListaU=self.Lista1[:]
		ListaO=self.Lista0[:]
		for i in range(333):
			ListaU=self.Lista1[:]
			ListaO=self.Lista0[:]
			for j in range(300):
				numero1=random.randint(0,len(ListaU)-1)
				numero2=random.randint(0, len(ListaO)-1)
				Uno=ListaU[numero1]
				Cero=ListaO[numero2]
				ListaU.append(Cero)
				ListaU.remove(Uno)
				ListaO.append(Uno)
				ListaO.remove(Cero)
				num=self.Diversidad2(ListaU)
				#~ C=((1-num)/Bmax)-((1-self.max)/Bmax)
				C=(1-(num/Bmax))-(1-(self.max/Bmax))
				#~ C=num-self.max
				#~ print "C : "+str(C)+"T : "+str(T)
				#~ print "div"+str(-C/T)
				expo=-C/T
				if(expo>=-760):
					#~ print "entro"
					expo=-759
				#~ exponencial=decimal.Decimal(0.0000000000000000000000000000000000000000000000000000000000000000000000000001)
				exponencial=math.e**(expo)
				#~ exponencial=numpy.exp(-C/T)
				#~ print "exp: "+str(exponencial)
				if(random.uniform(0,1) <exponencial  or C<0):
					self.Lista0=ListaO[:]
					self.Lista1=ListaU[:]
					self.max=num
				else:
					ListaU=self.Lista1[:]
					ListaO=self.Lista0[:]
			T=T/(1+B*T)



	def BT(self,semilla):
		self.Lista1=[]
		self.Lista0=[]
		self.sol=[]
		for i in range(self.n):
			self.sol.append(0)
			self.Lista0.append(i)
		random.seed(semilla)
		ListaTabu=[]
		TamListaTabu=50
		Frecuencia=[]
		for i in range(self.n):
			Frecuencia.append(0)
		SolAceptadas=1
#~Solucion aleatoria 
		for s in range(self.m):
			numeroAle =random.randint(0, self.n-1)
			while(numeroAle in self.Lista1):
				numeroAle =random.randint(0, self.n-1)
			self.sol[numeroAle]=1
			self.Lista1.append(numeroAle)
			self.Lista0.remove(numeroAle)
		self.FunBuena()
#~Comienzo de Busqueda tabu 
		ListaOG=self.Lista0[:]
		ListaUG=self.Lista1[:]
		maxG=self.max
		ListaOV=self.Lista0[:]
		ListaUV=self.Lista1[:]
		maxV=self.max
		for primerbucle in range(20):
			#~ print "pasamos"
			#~ print primerbucle
			NumeroAleatorio=random.uniform(0,1)
			if(NumeroAleatorio<0.25):
				#~ print"entramos 0"
				ListaU=[]
				ListaO=[]
				for i in range(self.n):
					ListaO.append(i)
				for s in range(self.m):
					numeroAle =random.randint(0, self.n-1)
					while(numeroAle in ListaU):
						numeroAle =random.randint(0, self.n-1)
					ListaU.append(numeroAle)
					ListaO.remove(numeroAle)
					max=self.Diversidad2(ListaU)
				self.Lista1=ListaU[:]
				self.Lista0=ListaO[:]
				self.max=max
			elif(NumeroAleatorio<0.5):
				#~ print"entramos 1"
				self.Lista0=ListaOG[:]
				self.Lista1=ListaUG[:]
				self.max=maxG
			else:
				#~ print "entramos ttt"
				self.Lista1=[]
				self.Lista0=[]
				for Cerospos in range(self.n):
					self.Lista0.append(Cerospos)
				while(len(self.Lista1)!=self.m):
					numeroAle1= random.uniform(0,1);
					punto= random.randint(0,len(self.Lista0)-1)
					Cero=self.Lista0[punto]
					#~ print"entramos 2"+str((1-Frecuencia[Cero])/(SolAceptadas))
					if(numeroAle1 < 1-((Frecuencia[Cero])/(SolAceptadas))):
						#~ print"entramos 2"+str((1-Frecuencia[Cero])/(SolAceptadas))
						self.Lista1.append(Cero)
						self.Lista0.remove(Cero)
				self.FunBuena()
			#~ Lista0=ListaU[:]
			#~ Lista1=ListaO[:]
			for segundobucle in range(5000):
				#~ print segundobucle
				ListaU=self.Lista1[:]
				ListaO=self.Lista0[:]
				AleUno=random.randint(0, len(self.Lista1)-1)
				AleCero=random.randint(0, len(self.Lista0)-1)
				Cero=ListaO[AleCero]
				Uno=ListaU[AleUno]
				ListaO.append(Uno)
				ListaU.append(Cero)
				ListaO.remove(Cero)
				ListaU.remove(Uno)
				numero=self.Diversidad2(ListaU)
				if(numero>maxG or not(ListaU in ListaTabu)):
					SolAceptadas=SolAceptadas+1;
					if(numero > maxV):
						ListaOV=ListaO[:]
						ListaUV=ListaU[:]
						maxV=numero
						if(numero < maxG):
							ListaUG=ListaU[:]
							ListaOG=ListaO[:]
							maxG=numero
					if(TamListaTabu>len(ListaTabu)):
						ListaTabu.append(ListaU[:])
					else:
						#~ ultimo=ListaTabu[len(ListaTabu)]
						ultimo=ListaTabu[0]
						ListaTabu.remove(ultimo)
						ListaTabu.append(ListaU[:])
						for cambio in range(len(ListaU)):
							Frecuencia[ListaU[cambio]]=Frecuencia[ListaU[cambio]]+1
				if( segundobucle%300 == 0):
					self.Lista1=ListaUV[:]
					self.Lista0=ListaOV[:]
					self.max=maxV
			ListaTabu=[]
			if(random.uniform(0,1)<0.5):
				TamListaTabu=TamListaTabu-0.25*TamListaTabu
			else:
				TamListaTabu=TamListaTabu+0.25*TamListaTabu

def menu():
	fichero='0'
	cadena="hacer \n0.-salir\n1.-Leer Fichero\n2.-Insertar Fichero lectura nuevo\n3.-Ver Resultados\n4.-Gredy\n5.-B.Aleatoria\n6.-B.Local\n7.-B.VNL\n\n8.-B.ES\n\n9.-B.BT\n"
	#~ entrada=raw_input(cadena)
	creado=0
	sol=0
	entrada=1000
	while(entrada<>"0"):
		entrada=raw_input(cadena)
		if(entrada=="1"):
			creado=1
			a=datos()
			a.LecturaL1(fichero)
			print "Fichero leido con exito"
		if(entrada=="2"):
			print "ficheros posibles son: 1.-"+"MDG-b_1_n500_m50.txt"+"2.-SOM-b_20_n500_m200.txt"+"3.-SOM-b_10_n300_m60.txt"
			Fic=raw_input()
			if(Fic=="1"):
				fichero="MDG-b_1_n500_m50.txt"
			elif(Fic=="2"):
				fichero="SOM-b_20_n500_m200.txt"
			elif(Fic=="3"):
				fichero="SOM-b_10_n300_m60.txt"
			else:
				print "ERROR FICHERO INEXISTENTE,inserte uno a mano"
				fichero=raw_input("Nombre del fichero, con la extension")
		if(entrada=="3"):
			if(sol==0):
				print"Realice un Algoritmo para obtener un resultado"
			else:
				print "Solucion: "
				aux =a.sol
				print a.Lista1
				a.FunBuena()
				print a.max
				print len(a.sol)
		if(entrada=="4"):
			if(creado==0):
				print "Lea el fichero,para realizar el algoritmo Gredy"
			else:
				print "Realizando Gredy"
				a.Gredy()
				print "Gredy terminado"
				sol=1
		if(entrada=="5"):
			if(creado==0):
				print "Lea el fichero,pra realizar la busqueda aleatoria"
			else:
				semilla=raw_input("Inserte semilla: ")
				print "Realizando Busqueda Aleatoria"
				a.BA(semilla)
				print "Busqueda aleatoria terminada"
				sol=1
		if(entrada=="6"):
			if(creado==0):
				print "Lea el fichero,pra realizar la busqueda Local"
			else:
				semilla=raw_input("Inserte semilla: ")
				a.BL(semilla)
				sol=1
		if(entrada=="7"):
			if(creado==0):
				print "Lea el fichero,pra realizar la busqueda VNL"
			else:
				semilla=raw_input("Inserte semilla: ")
				a.BVND(semilla)
				sol=1
		if(entrada=="8"):
			if(creado==0):
				print "Lea el fichero,pra realizar la busqueda Temple Simulado"
			else:
				semilla=raw_input("Inserte semilla: ")
				a.TS(semilla)
				sol=1
		if(entrada=="9"):
			if(creado==0):
				print "Lea el fichero,pra realizar la busqueda Busqueda Tabu"
			else:
				semilla=raw_input("Inserte semilla: ")
				a.BT(semilla)
				sol=1
	







def ficherito():
	try:
		f= open("datos.txt")       
	except IOError:                     
		print "El fichero no existe, ERROR"
		exit("error en la gestion de ficheros 2")
	Ficheros=[]
	Semillas=[]
	dato = f.readline()
	num=dato.split()
	Algoritmo=int(num[0])
	numeroSemillas=int(num[1])
	numeroFicheros=int(num[2])
	for i in range(numeroSemillas):
		dato = f.readline()
		num=dato.split()
		Semillas.append(int(num[0]))
	for i in range(numeroFicheros):
		dato = f.readline()
		num=dato.split()
		Ficheros.append(str(num[0]))
	f.close()
	
	print "leido\n"
	print Semillas
	print Ficheros
	if(Algoritmo==0):
		print "Gredy\n"
		f=open("SolucionGredy.txt","w")
		f.write("Gredy:\n")
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a.LecturaL1(Ficheros[i])
			a.Gredy()
			f.write("\tFichero:"+str(Ficheros[i])+"\n")
			f.write("\t  max:"+str(a.max)+"\n")
	if(Algoritmo==1):
		print "BA\n"
		#~ f=open("SolucionBusquedaAleatoria.txt","w")
		#~ f.write("Busqueda Aleatoria:\n")
		cadena="Busqueda Aleatoria:\n"
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a.LecturaL1(Ficheros[i])
			#~ f.write("\tFichero:"+str(Ficheros[i])+"\n")
			cadena=cadena+"\tFichero:"+str(Ficheros[i])+"\n"
			for j in range(len(Semillas)):
				a.BA(Semillas[j])
				print "realizada/s "+str(j)+"semilla/s"+str(a.max)
				#~ f.write("\t  Semilla:"+str(Semillas[j])+"\n")
				#~ f.write("\t    max:"+str(a.max)+"\n")
				cadena=cadena+"\t  Semilla:"+str(Semillas[j])+"\n"+"\t    max:"+str(a.max)+"\n"
		f=open("SolucionBusquedaAleatoria.txt","w")
		f.write(cadena)
		f.close()
	if(Algoritmo==2):
		print "BL\n"
		#~ f=open("SolucionBusquedaLocal.txt","w")
		#~ f.write("Busqueda Local:\n")
		cadena="Busqueda Local:\n"
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a.LecturaL1(Ficheros[i])
			#~ f.write("\tFichero:"+str(Ficheros[i])+"\n")
			cadena=cadena+"\tFichero:"+str(Ficheros[i])+"\n"
			for j in range(len(Semillas)):
				a.BL(Semillas[j])
				print "realizada/s "+str(j)+"semilla/s"+str(a.max)
				#~ f.write("\t  Semilla:"+str(Semillas[j])+"\n")
				cadena=cadena+"\t  Semilla:"+str(Semillas[j])+"\n"
				#~ f.write("\t    max:"+str(a.max)+"\n")
				cadena=cadena+"\t    max:"+str(a.max)+"\n"
		f=open("SolucionBusquedaLocal.txt","w")
		f.write(cadena)
		f.close()
	if(Algoritmo==3):
		print "BVND\n"
		#~ f=open("SolucionBusquedaVND.txt","w")
		#~ f.write("Busqueda VND:\n")
		cadena="Busqueda VND:\n"
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a=datos()
			a.LecturaL1(Ficheros[i])
			#~ f.write("\tFichero:"+str(Ficheros[i])+"\n")
			cadena=cadena+"\tFichero:"+str(Ficheros[i])+"\n"
			for j in range(len(Semillas)):
				a.BVND(Semillas[j])
				print "realizada/s "+str(j)+"semilla/s"+str(a.max)
				#~ f.write("\t  Semilla:"+str(Semillas[j])+"\n")
				#~ f.write("\t    max:"+str(a.max)+"\n")
				cadena=cadena+"\t  Semilla:"+str(Semillas[j])+"\n"+"\t    max:"+str(a.max)+"\n"
		f=open("SolucionBusquedaVND.txt","w")
		f.write(cadena)
	if(Algoritmo==4):
		print "Temple Simulado\n"
		cadena="TempleSimulado:\n"
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a=datos()
			a.LecturaL1(Ficheros[i])
			#~ f.write("\tFichero:"+str(Ficheros[i])+"\n")
			cadena=cadena+"\tFichero:"+str(Ficheros[i])+"\n"
			for j in range(len(Semillas)):
				a.TS(Semillas[j])
				print "realizada/s "+str(j)+"semilla/s max: "+str(a.max)
				cadena=cadena+"\t  Semilla:"+str(Semillas[j])+"\n"+"\t    max:"+str(a.max)+"\n"
		f=open("SolucionTempleSimulado.txt","w")
		f.write(cadena)
		f.close()
	if(Algoritmo==5):
		print "Busqueda Tabu\n"
		cadena="Busqueda Tabu:\n"
		a=datos()
		for i in range(len(Ficheros)):
			print "Fichero numero: "+str(i)+" nombre: "+str(Ficheros[i])
			a=datos()
			a.LecturaL1(Ficheros[i])
			#~ f.write("\tFichero:"+str(Ficheros[i])+"\n")
			cadena=cadena+"\tFichero:"+str(Ficheros[i])+"\n"
			for j in range(len(Semillas)):
				a.BT(Semillas[j])
				print "realizada/s "+str(j)+"semilla/s max: "+str(a.max)
				cadena=cadena+"\t  Semilla:"+str(Semillas[j])+"\n"+"\t    max:"+str(a.max)+"\n"
		f=open("SolucionBusquedaTabu.txt","w")
		f.write(cadena)
		f.close()
	f.close()




def main():
	#~ menu()
	ficherito()

	return 0

if __name__ == '__main__': main()






#~ class random:
	#~ def Set_random (self,x):/* Inicializa la semilla al valor x.Solo debe llamarse a esta funcion una vez en todo el programa */
		#~ Seed = x

	#~ def Get_random (self): Devuelve el valor actual de la semilla */
		#~ return Seed

	#~ def Rand(self):/* Genera un numero aleatorio real en el intervalo [0,1[(incluyendo el 0 pero sin incluir el 1) */
		#~ Seed = (Seed * PRIME) and MASK
		#~ return ( Seed * SCALE)

	#~ def Randint(self,low,high):/* Genera un numero aleatorio entero en {low,...,high} */
		#~ return int ((low + (high-(low)+1) * self.Rand()))

	#~ def Randfloat(self,low,high):/* Genera un numero aleatorio real en el intervalo [low,...,high[(incluyendo 'low' pero sin incluir 'high') */
		#~ return (low + (high-(low))*self.Rand())



#"MDG-b_1_n500_m50.txt"
#"SOM-b_20_n500_m200.txt"
#"SOM-b_10_n500_m60.txt"


	#~ for i in range(len(a.sol)):
		#~ if(a.sol[i]==1):
			#~ cont=cont+1
	#~ print cont
	#~ for y in range(2, 500):
		#~ aux=a.ma[0,y] 
		#~ if(max<aux):
			#~ pos=y
			#~ max=aux
	#~ a.sol[y]=1
	#~ print str(y) +" "+str(max)


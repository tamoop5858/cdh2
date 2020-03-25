package test

class test1 {

	static void main(args) {

		def gString = "groovy string"

		String jString = "java string"
		String jString2 = "${jString}"

		println gString

		println jString2

		char aznable = 'c';    // 赤い彗星

		boolean jBool = true; // boolean型

		def map = [ name : "ぽんた", age : 20, attend : true]
		def hashMap = [ name : "ぽんた", age : 20, attend : true] as HashMap
		TreeMap treeMap = [ name : "ぽんた", age : 20, attend : true]

		println treeMap


		def list = [1, 2, 3]
		def linkedList = [1, 2, 3] as LinkedList
		LinkedList linkedList2 = [1, 2, 3]

		println linkedList2
		disp linkedList2

		def a = "aa"

		def add = {x, y -> x + y}

		//def minus =(x, y){ x -y }

		disp a
		disp add(1,4)

		for(String s : ["a", "b", "c"]){
			disp s
		}

		def list22 = [1,2,3] // ArrayListになる。
		def map22 = [key1:4, key2:"value2"] // LinkedHashMapになる。

		disp list22
		disp map22

		for (String s : map22.keySet()) {
			disp s
			disp map22.get(s)
		}

		def aaaa = "1" as Integer
		disp  1 == aaaa
	}

	def static disp(variable) {
		println "[ " + variable.class.toString() + " : ${variable} ]"
	}

}
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int searchCost(char fromEki[], int size, int weight);


int main(void) {
	int cost = searchCost("有楽町", 170, 2);
	printf("値段：%d \n", cost);
}


int searchCost(char fromEki[], int size, int weight){
	FILE *fp; 
	char fname[] = "base_cost.txt";
	char line[256];
	char eki[16];
	int cost;
	int result1;

	fp = fopen(fname, "r"); 
	if(fp == NULL){
		printf("[ERROR]:データが読み込めませんでした.\n");
		return 1;
	}
	while(fgets(line, 41, fp) != NULL) {
		sscanf(line, "%s %d", eki, &cost);
		if (strcmp(eki, fromEki) == 0) {
			result1 = cost;
			break;
		}
	}
	fclose(fp); // ファイルを閉じる

	FILE *fp2; 
	char fname2[] = "size.txt";
	char line2[256];
	int cost2;
	int size2;
	int weight2;
	int result2;
	int befor_size;

	fp2 = fopen(fname2, "r"); 
	if(fp2 == NULL){
		printf("[ERROR]:データが読み込めませんでした.\n");
		return 1;
	}
	int end_flg = 1;
	int last_size;
	int last_cost;
	while(fgets(line2, 41, fp2) != NULL) {
		sscanf(line2, "%d %d %d", &size2, &weight2, &cost2);
		if (size > befor_size && size <= size2 ) {
			result2 = cost2;
			break;			
		} 
		befor_size = size2;
		last_size = size2;
		last_cost = cost2;
	}
	fclose(fp2); // ファイルを閉じる
	if (size > last_size) {
		result2 = last_cost;
	}

	return (result1 + result2) * 1.1; 
}

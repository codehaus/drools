SHELL	= /bin/sh 
NULL	= 
ECHO	= 
default: latex

cleanLatex:
	rm  -rf document.pdf document.dvi document.ps 
	rm  -rf *.ind *.idx *.ilg *.log *.lof *.aux *.out *.lot *.toc  
 
 
latex: cleanLatex
	latex document.tex 
	makeindex document
	latex document.tex 
	pdflatex document.tex 
	rm  -rf *.ind *.idx *.ilg *.log *.lof *.aux *.out *.lot *.toc  

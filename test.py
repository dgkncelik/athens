import lucene

from java.io import StringReader
from org.apache.lucene.analysis.ja import JapaneseAnalyzer
from org.apache.lucene.analysis.standard import StandardAnalyzer, StandardTokenizer
from org.apache.lucene.analysis.tokenattributes import CharTermAttribute
from org.apache.lucene.index.memory import MemoryIndex


lucene.initVM(vmargs=['-Djava.awt.headless=true'])

# Basic tokenizer example.
test = "This is how we do it."
tokenizer = StandardTokenizer()
tokenizer.setReader(StringReader(test))

charTermAttrib = tokenizer.getAttribute(CharTermAttribute.class_)
tokenizer.reset()
tokens = []
while tokenizer.incrementToken():
    tokens.append(charTermAttrib.toString())

print(tokens)
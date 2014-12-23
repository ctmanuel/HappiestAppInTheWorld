"""
Make sure you have Python 3!!!

This script will run automatically and begin collecting compliments. It will
write them to 'compliments.xml' as new unique compliments are discovered

It takes a while because web requests take time...
"""
import urllib.request
import time
import threading

some_rlock = threading.RLock()

compliment_url = 'http://toykeeper.net/programs/mad/compliments'

start_tag = '<h3 class="blurb_title_1">'
end_tag = '</h3>'

file_to_write_to = "compliments.xml"
f = open(file_to_write_to, "w") 

def write_compliment(comp_str):
	"""
	Writes the compliment to a file using Andoid XML formatting
	"""
	f.write("		<item >")
	f.write(comp_str)
	f.write("</item>\n")

def get_compliment():
	"""
	Requests the html src for the compliment website, then searches for
	and returns the compliment
	"""
	page = urllib.request.urlopen(compliment_url)
	page_src = str(page.read())
	
	start_of_tag = page_src.find(start_tag)
	end_of_tag   = page_src.find(end_tag, start_of_tag + len(start_tag))
	
	compliment = page_src[ start_of_tag + len(start_tag):end_of_tag-4]
	return compliment

comps = {}  # used to keep track of compliments found
def num_compliments():
	""" Prints compliment statistics """
	num_unique_compliments = len(comps.keys())
	print( "Unique Compliments: ", num_unique_compliments)

def collect_compliments(thread=""):
	"""
	Start collecting some compliments. Only save unique ones
	"""
	samples = 100
	for i in range(samples):
		new_comp = get_compliment()
		
		with some_rlock: # Locked resources!
			if len(new_comp) > 2: # parser sends empty string as error	
				if new_comp not in comps:
					write_compliment(new_comp)
					comps[new_comp] = True

		if i % 10 == 0:
			print(thread,": ", int(i/samples*100.0), "%")
			num_compliments()

class ComplimentCollectinThread (threading.Thread):
	"""
	A basic thread for collecting compliments. I am unsure
	about how to kill these threads once the program starts....
	"""
	def __init__(self, name):
		threading.Thread.__init__(self)
		self.name = name

	def run(self):
		print( "Starting " + self.name)
		collect_compliments(thread=self.name)
		print( "Exiting " + self.name)


### RUN IT
num_threads = 40
threads = []
for x in range(num_threads):
	threads.append( ComplimentCollectinThread("Thread: " + str(x)))
	threads[x].start()

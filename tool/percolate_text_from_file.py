import requests
import time

text_file_location = '/home/dgkn/Documents/athens/sample_query_and_text/documents'
athens_percolation_url = 'http://localhost:8080/api/percolate?text='
file_object = open(text_file_location, 'r')
lines = file_object.readlines()
counter = 1
total_time = 0
for line in lines:
    start_time = time.time()
    _line = line.replace('\n', '').replace('\r', '')
    _url = '%s%s' % (athens_percolation_url, line)
    r = requests.get(url=_url)
    end_time = time.time()
    time_take = end_time - start_time
    print('---- <TIME: %s> ----' % time_take)
    print('%s %s result=%s' % (counter, _line[:20], r.content[:100]))
    counter = counter + 1
    total_time = total_time + time_take

print('TOTAL TIME TAKE=%s' % total_time)
print('TOTAL DOCUMENT COUNT=%s' % counter)
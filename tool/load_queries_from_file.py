import requests

query_file_location = '/home/dgkn/Documents/athens/sample_query_and_text/queries'
athens_api_url = 'http://localhost:8080/api/add_query'
file_object = open(query_file_location, 'r')
lines = file_object.readlines()
counter = 1
for line in lines:
    _line = line.replace('\n', '').replace('\r', '')
    query_object = {"queryString": _line}
    r = requests.post(url=athens_api_url, json=query_object)
    print('%s %s %s' % (counter, r.content, _line))
    counter = counter + 1

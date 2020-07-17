from elasticsearch import Elasticsearch
import requests

OK_COUNT = 0
ERROR_COUNT = 0
TOTAL_COUNT = 0
file_name = "/home/user/queries"
query_field_name = "query"
query_replace = ""
es_doc_type = "query"
source_es_uri = "http://127.0.0.1:9200"
athens_uri_pattern = "http://127.0.0.1:8080/api/add_query?query="
source_engine_es = Elasticsearch(hosts=source_es_uri, timeout=180)
scroll = '30m'
es_size = 5000
q = {
    "size": 300
}

FILE_WRITE = open(file_name, 'w+')


def save_docs(hits):
    for h in hits:
        _id = h['_id']
        if query_field_name not in h['_source']:
            continue
        q = h['_source'][query_field_name]
        q = q.replace(query_replace, '')
        FILE_WRITE.write(q + '\n')
        print('%s %s' % (_id, q))


count = 0
total_count = 0
scroll_id = None
while True:
    if not scroll_id:
        search_result = source_engine_es.search(index='_all',
                                                doc_type=es_doc_type,
                                                scroll=scroll,
                                                size=es_size,
                                                request_timeout=300,
                                                body=q)
        total_count = search_result['hits']['total']
        count = len(search_result['hits']['hits'])
    else:
        search_result = source_engine_es.scroll(scroll=scroll,
                                                scroll_id=scroll_id,
                                                request_timeout=300)
        count += len(search_result['hits']['hits'])

    scroll_id = search_result['_scroll_id']
    save_docs(search_result['hits']['hits'])
    print('%d/%d' % (count, total_count))
    if count >= total_count:
        break
FILE_WRITE.close()

FILE_READ = open(file_name, 'r+')
lines = FILE_READ.readlines()
for line in lines:
    _line = line.replace('\n', '')
    url = '%s%s' % (athens_uri_pattern, _line)
    response = requests.get(url)
    if response.text == 'OK':
        print('%s is OK' % _line)
        OK_COUNT = OK_COUNT + 1
    else:
        print('%s ERROR' % _line)
        ERROR_COUNT = ERROR_COUNT + 1
    TOTAL_COUNT = TOTAL_COUNT + 1
FILE_READ.close()
print('TOTAL:%s OK:%s ERROR:%s' % (TOTAL_COUNT, OK_COUNT, ERROR_COUNT))

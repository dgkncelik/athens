from pymongo import MongoClient
import time
import requests

mongo_host = 'mongodb://admin:test@127.0.0.1:27017'
mongo_database = 'TESTDB'
mongo_collection = 'TESTCOLLECTION'
mongo_document_limit = 1000
mongo_query = {}
mongo_field = 'text'
athens_percolate_uri = 'http://127.0.0.1:8080/api/percolate?document='


# ref: https://medium.com/pythonhive/python-decorator-to-measure-the-execution-time-of-methods-fa04cb6bb36d
def timeit(method):
    def timed(*args, **kw):
        ts = time.time()
        result = method(*args, **kw)
        te = time.time()

        if 'log_time' in kw:
            name = kw.get('log_name', method.__name__.upper())
            kw['log_time'][name] = int((te - ts) * 1000)
        else:
            print('## Time:  %2.2f ms' % ((te - ts) * 1000))
        return result

    return timed


@timeit
def percolate(string):
    url = '%s%s' % (athens_percolate_uri, string)
    response = requests.get(url)
    print('## String: %s' % string[:100])
    print('## Percolation Result: %s' % response.text[:100])


cursor = MongoClient(host=mongo_host).get_database(mongo_database).get_collection(mongo_collection).find(
    mongo_query).sort([('_id', -1)]).limit(mongo_document_limit)
for doc in cursor:
    s = doc[mongo_field]
    percolate(s)
    print('-----')

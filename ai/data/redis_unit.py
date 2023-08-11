import redis
import numpy as np

from data.redis_define import HOST, ALL_CODE_REDIS_KEY

client = redis.Redis(host=HOST, decode_responses=True)


class Redis:
    __client = {}

    def __init__(self):
        self.__client = client

    def get_list_float32_by_key(self, key):
        return np.array(self.get_list_by_key(key),  dtype=np.float32)

    def get_list_by_key(self, key):
        lists = self.__client.lrange(key, 0, -1)
        # print('redis key: ', key, ' data len : ', len(lists))
        return lists

    def get_keys_iter_by_scan(self, key):
        return self.__client.scan_iter(match=key)

    def get_all_codes_list(self):
        return self.__client.lrange(ALL_CODE_REDIS_KEY, 0, -1)

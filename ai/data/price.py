import numpy as np
from sklearn.preprocessing import MinMaxScaler
from data.data_utils import BaseDataUtils
from data.redis_unit import Redis
from data.redis_define import PRICE_REDIS_PREFIX


class PriceData(BaseDataUtils):
    redis_client = Redis()

    """"
        根据code，从Redis读取数据，对每一批数据归一化，最后统计添加到训练数据集
    """
    def read_and_normal(self, match_code, time_steps, future_days):
        codes = self.redis_client.get_all_codes_list()
        x_all = list()
        y_all = list()
        for code in codes:
            if match_code in code:
                try:
                    read_data = self.redis_client.get_list_float32_by_key(PRICE_REDIS_PREFIX + code)
                    scaler = MinMaxScaler()
                    normal_data = scaler.fit_transform(read_data.reshape(-1, 1))
                    x_samples, y_samples = super().split_data(normal_data, time_steps, future_days, 0)

                    x_all = np.append(x_all, x_samples)
                    y_all = np.append(y_all, y_samples)
                except Exception:
                    print(Exception)

        x = np.array(x_all,  dtype=np.float32)
        y = np.array(y_all, dtype=np.float32)

        return x.reshape(int(x.shape[0]/time_steps), time_steps, 1), y.reshape(int(y.shape[0]), 1)

    """" 
        输出训练数据集
    """
    def out_data(self, match_code, time_steps, test_records, future_days):
        x_all, y_all = self.read_and_normal(match_code, time_steps, future_days)
        return super().tran_test_data(x_all, y_all, test_records)














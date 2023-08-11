from sklearn.preprocessing import MinMaxScaler
import numpy as np

from data.price import PriceData
from data.redis_unit import Redis
from data.redis_define import PRICE_REDIS_PREFIX, TURN_OVER_REDIS_PREFIX


class PriceTurnOverData(PriceData):
    redis_client = Redis()

    def __combine_data(self, data1, data2):
        normal_data = list()
        num = len(data1)
        if num != len(data2):
            return

        for i in range(num):
            normal_data.append(data1[i])
            normal_data.append(data2[i])

        ret = np.array(normal_data, dtype=np.float32)
        return ret.reshape(num, 2)

    def read_one_code_and_normal(self, code):
        price_key = PRICE_REDIS_PREFIX + code
        turn_over_key = TURN_OVER_REDIS_PREFIX + code

        price_data = self.redis_client.get_list_float32_by_key(price_key)
        turn_over_data = self.redis_client.get_list_float32_by_key(turn_over_key)

        price_scaler = MinMaxScaler()
        turn_over_scaler = MinMaxScaler()
        price_normal_data = price_scaler.fit_transform(price_data.reshape(-1, 1))
        turn_over_normal_data = turn_over_scaler.fit_transform(turn_over_data.reshape(-1, 1))

        return self.__combine_data(price_normal_data, turn_over_normal_data), price_scaler, price_data[-1:].tolist()[0]

    def read_and_normal(self, match_code, time_steps, future_days):
        codes = self.redis_client.get_all_codes_list()
        x_all = list()
        y_all = list()
        for code in codes:
            if match_code in code:
                try:
                    normal_data, price_scaler, current_price = self.read_one_code_and_normal(code)
                    x_samples, y_samples = super().split_data(normal_data, time_steps, future_days, 0)

                    x_all = np.append(x_all, x_samples)
                    y_all = np.append(y_all, y_samples)
                except Exception:
                    print(Exception)

        x = np.array(x_all, dtype=np.float32)
        y = np.array(y_all, dtype=np.float32)

        return x.reshape(int(x.shape[0]/time_steps/2), time_steps, 2), y.reshape(int(y.shape[0]), 1)



import os

from data.price_turn_over import PriceTurnOverData
from lstm.lstm_sample_mode import LstmMode
from data.price import PriceData
"""
    说明：time_steps 历史天数， FUTURE_DAYS 预测未来天数的价格
    time_steps = 60  ==> FUTURE_DAYS = 10  （历史60天，预测未来10）
    time_steps = 30  ==> FUTURE_DAYS = 5  （历史30天，预测未来5）
    time_steps = 20  ==> FUTURE_DAYS = 3  （历史20天，预测未来3）
    time_steps = 15  ==> FUTURE_DAYS = 2  （历史15天，预测未来2）
    time_steps = 10  ==> FUTURE_DAYS = 1  （历史10天，预测未来1）
"""
CONFIG = [
    {
        'time_steps': 60,
        'future_days': 5
    },
    {
        'time_steps': 15,
        'future_days': 2
    },
    {
        'time_steps': 10,
        'future_days': 2
    },
    {
        'time_steps': 10,
        'future_days': 1
    },
    {
        'time_steps': 5,
        'future_days': 1
    }
]


"""
    训练数据集
"""
REDIS_SCAN_MATCH_KEY = '3003'
TEST_RECORDS = 100
BATCH_SIZE = 100
EPOCHS = 10

MODE_WEIGHTS_PATH_PREFIX = '../mode/lstm/priceTurnOverMode/'
MODE_WEIGHTS_PATH_SUFFIX = '/weights'


class PriceTurnOverMode(LstmMode):

    def __init__(self):
        LstmMode.__init__(self, CONFIG, MODE_WEIGHTS_PATH_PREFIX)


if __name__ == '__main__':
    for one in CONFIG:
        time_steps = one.get('time_steps')
        future_days = one.get('future_days')
        x_train, y_train, x_test, y_test = PriceTurnOverData().out_data(REDIS_SCAN_MATCH_KEY, time_steps, TEST_RECORDS, future_days)
        PriceTurnOverMode().train_mode(x_train, y_train, x_test, y_test, time_steps, future_days, BATCH_SIZE, EPOCHS, MODE_WEIGHTS_PATH_PREFIX)

class BaseDataUtils:
    def predict_data(self, data, time_steps, in_dim):
        x_predict = list()
        if len(data) >= time_steps:
            x_predict = data[-time_steps:]

        print('\n#### Predict Data shape ####')
        print(x_predict.shape)
        return x_predict.reshape(1, time_steps, in_dim)

    """
        data 同一时间输入特征数据 [A ,B ,C, D]
        y_local 待预测特征在data的位置，比如待预测特征为A，则为0，待预测特征为B , 1
    """
    def split_data(self, data, time_steps, future_days, y_local):
        x_samples = list()
        y_samples = list()

        num_of_rows = len(data)
        for i in range(time_steps, num_of_rows - future_days, 1):
            x_sample = data[i - time_steps:i]
            y_sample = data[i + future_days - 1][y_local]
            x_samples.append(x_sample)
            y_samples.append(y_sample)

        return x_samples, y_samples

    def __train_data(self, x_data, y_data, test_records):
        x_train = x_data[:-test_records]
        y_train = y_data[:-test_records]

        print('\n#### Training Data shape ####')
        print(x_train.shape)
        print(y_train.shape)
        return x_train, y_train

    def __test_data(self, x_data, y_data, test_records):
        x_test = x_data[-test_records:]
        y_test = y_data[-test_records:]
        print('\n#### Testing Data shape ####')
        print(x_test.shape)
        print(y_test.shape)
        return x_test, y_test

    """" 
        输出训练数据集
    """
    def tran_test_data(self, x_all, y_all, test_records):
        x_train, y_train = self.__train_data(x_all, y_all, test_records)
        x_test, y_test = self.__test_data(x_all, y_all, test_records)
        return x_train, y_train, x_test, y_test














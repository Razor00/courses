'''
(c) 2011, 2012 Georgia Tech Research Corporation
This source code is released under the New BSD license.  Please see
http://wiki.quantsoftware.org/index.php?title=QSTK_License
for license details.

Created on January, 24, 2013

@author: Sourabh Bajaj
@contact: sourabhbajaj@gatech.edu
@summary: Example tutorial code.
'''

# QSTK Imports
import QSTK.qstkutil.qsdateutil as du
import QSTK.qstkutil.tsutil as tsu
import QSTK.qstkutil.DataAccess as da

# Third Party Imports
import datetime as dt
import matplotlib.pyplot as plt
import pandas as pd

import numpy as np
import math
print "Pandas Version", pd.__version__

def get_data(dt_start, dt_end, ls_symbols, allocation):

    print allocation
    # We need closing prices so the timestamp should be hours=16.
    dt_timeofday = dt.timedelta(hours=16)

    # Get a list of trading days between the start and the end.
    ldt_timestamps = du.getNYSEdays(dt_start, dt_end, dt_timeofday)

    # Creating an object of the dataaccess class with Yahoo as the source.
    c_dataobj = da.DataAccess('Yahoo', cachestalltime=0)

    # Keys to be read from the data, it is good to read everything in one go.
    #ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ls_keys = ['close']

    # Reading the data, now d_data is a dictionary with the keys above.
    # Timestamps and symbols are the ones that were specified before.
    ldf_data = c_dataobj.get_data(ldt_timestamps, ls_symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))

    # Filling the data for NAN
    for s_key in ls_keys:
        d_data[s_key] = d_data[s_key].fillna(method='ffill')
        d_data[s_key] = d_data[s_key].fillna(method='bfill')
        d_data[s_key] = d_data[s_key].fillna(1.0)

    # Getting the numpy ndarray of close prices.
    na_price = d_data['close'].values


    # Normalizing the prices to start at 1 and see relative returns
    na_normalized_price = na_price / na_price[0, :]

    # Copy the normalized prices to a new ndarry to find returns.
    na_rets = (na_normalized_price.copy()* allocation)

    # daily_pf_value = np.array([sum(day) for day in na_rets])
    daily_pf_value = np.sum(na_rets, axis=1)

    #  Calculate the daily returns of the prices. (Inplace calculation)
    # returnize0 works on ndarray and not dataframes.
    cum_ret = daily_pf_value[250]
    tsu.returnize0(daily_pf_value)

    avg_daily_ret =  np.average(daily_pf_value)
    std_daily_ret =  np.std(daily_pf_value)
    sharpe_ratio  = math.sqrt(252) * avg_daily_ret/std_daily_ret

    return avg_daily_ret, sharpe_ratio, cum_ret


def simulate(dt_start, dt_end, ls_symbols):
    best_allocation = []
    best_sharpe = -100000000000000
    n = 0
    while n <= 9999:
        allocation = [n/1000, (n/100)%10, (n/10)%10, n%10]
        n += 1
        if sum(allocation) != 10:
            continue
        allocation[0] = allocation[0]/10.0
        allocation[1] = allocation[1]/10.0
        allocation[2] = allocation[2]/10.0
        allocation[3] = allocation[3]/10.0

        daily_ret, sharpe, cum_ret = get_data(dt_start, dt_end, ls_symbols, allocation)
        if sharpe > best_sharpe:
            best_sharpe = sharpe
            best_allocation = allocation

    count = 0
    while count < 4:
        allocation = [0, 0, 0, 0]
        allocation[count] = 1
        daily_ret, sharpe, cum_ret = get_data(dt_start, dt_end, ls_symbols, allocation)
        if sharpe > best_sharpe:
            best_sharpe = sharpe
            best_allocation = allocation
        count += 1

    return best_allocation

def main():
    ''' Main Function'''
    #ls_symbols = ["AAPL", "GLD", "GOOG", "$SPX", "XOM"]
#    ls_symbols = ["AAPL", "GLD", "GOOG", "XOM"]
#    allocations = [0.4, 0.4, 0.0, 0.2]
#    ls_symbols = ["AXP", "HPQ", "IBM", "HNZ"][0.4, 0.4,0.0,0.2])
#    allocations = [0.0, 0.0, 0.0, 1.0]

    # Start and End date of the charts
    #dt_start = dt.datetime(2010, 1, 1)
    #dt_end = dt.datetime(2010, 12, 31)
# question 1
#    ls_symbols=['C', 'GS', 'IBM', 'HNZ']
#    dt_start = dt.datetime(2010, 1, 1)
#    dt_end = dt.datetime(2010, 12, 31)

#    vol = simulate(dt_start, dt_end, ls_symbols)

#    daily_ret, sharpe, cum_ret = get_data(dt_start, dt_end, ls_symbols, vol)
#    print 'Allocation       :', vol
#    print 'sharpe ratio     :', sharpe
#    print 'average daily ret:', daily_ret
#   print 'average daily ret {:.2%}'.format(avg_daily_ret)
#    print 'cumulative return:', cum_ret
#question 2
    ls_symbols=['BRCM', 'TXN', 'AMD', 'ADI']
    dt_start = dt.datetime(2011, 1, 1)
    dt_end = dt.datetime(2011, 12, 31)

    vol = [0.0, 0.0, 0.1, 0.9]

    daily_ret, sharpe, cum_ret = get_data(dt_start, dt_end, ls_symbols, vol)
    print 'Allocation       :', vol
    print 'sharpe ratio     :', sharpe
    print 'average daily ret:', daily_ret
    print 'cumulative return:', cum_ret




if __name__ == '__main__':
    main()

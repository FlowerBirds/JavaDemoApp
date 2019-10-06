#! /usr/bin/env python
# -*- coding: utf-8 -*-

import pythoncom
import pyHook
import queue
import time
import thread
import sqlite3

# 存放数据队列
data_queue = queue.Queue()

'''
数据格式：
时间：
type：鼠标，键盘
ascii：
key：

'''
'''
513: Left Down
516: Right Down
'''
def onMouseEvent(event):
    # 监听鼠标事件
    if event.Message == 513 or event.Message == 516:
        click_date = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
        key = 'right'
        if event.Message == 513:
            key = 'left'
        data_queue.put((click_date, 'mouse', event.Message, key))
    # print "MessageName:", event.MessageName
    # print "Message:", event.Message
    # print "Time:", event.Time
    # print "Window:", event.Window
    # print "WindowName:", event.WindowName
    # print "Position:", event.Position
    # print "Wheel:", event.Wheel
    # print "Injected:", event.Injected
    # print "---"
    # 返回 True 以便将事件传给其它处理程序
    # 注意，这儿如果返回 False ，则鼠标事件将被全部拦截
    # 也就是说你的鼠标看起来会僵在那儿，似乎失去响应了
    return True

def onKeyboardEvent(event):
    # 监听键盘事件
    click_date = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    data_queue.put((click_date, 'keyboard', event.Ascii, event.Key))
    # print "MessageName:", event.MessageName
    # print "Message:", event.Message
    # print "Time:", time.time()
    # print time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    # print "Window:", event.Window
    # print "WindowName:", event.WindowName
    # print "Ascii:", event.Ascii, chr(event.Ascii)
    # print "Key:", event.Key
    # print "KeyID:", event.KeyID
    # print "ScanCode:", event.ScanCode
    # print "Extended:", event.Extended
    # print "Injected:", event.Injected
    # print "Alt", event.Alt
    # print "Transition", event.Transition
    # print "---"

    # 同鼠标事件监听函数的返回值
    return True

def print_data(name):
    print 'Start recorder', name
    conn = sqlite3.connect("keyboard.db")
    c = conn.cursor()
    c.execute('''create table if not exists pc_event
        (date text, type text, ascii integer, key char(10))
    ''')

    while True:
        data = data_queue.get()
        c.execute("insert into pc_event values('%s', '%s', %s, '%s')" % data)
        conn.commit()

def main():
    # 创建一个“钩子”管理对象
    hm = pyHook.HookManager()

    # 监听所有键盘事件
    hm.KeyDown = onKeyboardEvent
    # 设置键盘“钩子”
    hm.HookKeyboard()

    # 监听所有鼠标事件
    hm.MouseAll = onMouseEvent
    # 设置鼠标“钩子”
    hm.HookMouse()

    thread.start_new_thread(print_data, ("Thead-01",))
    # 进入循环，如不手动关闭，程序将一直处于监听状态
    pythoncom.PumpMessages(10000)


if __name__ == "__main__":
    main()
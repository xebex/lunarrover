月球车

程序模拟了5个月球车和1个控制中心。设计如下：
1. 地图的生成：为简化设计，在程序中使用二维数组构成的网格代表地图，可调整Atlas中的ROWS和COLS调整地图的大小。如果将二维数组中某个元素置为false，则代表该位置是障碍。
2. 规划线路：*.txt文件代表规划的线路，由月球车在启动时读取。线路格式说明见txt文件中的注释。txt文件可手工编写，也可以通过Generator自动生成。在txt文件中只是规划的线路，并不考虑障碍的情况。月球车在实际运行过程中会探测障碍并绕行。
3. 方向：为简化设计，月球车运行方向只支持E、S、W、N四种，且转向时只支持顺时针旋转。
4. 速度：月球车速度单位为“格/秒”。
5. 每个月球车分别启动一个线程执行，共5个月球车线程。控制中心也是1个独立线程。
6. 月球车编号为1到5，分别从1.txt到5.txt中读取规划的线路。月球车尽量按照规划的线路运行，但遇到障碍时会自动绕行。绕行规则是：使用BFS（广度优先搜索）寻找从当前格到规划线路的下一个格的最短路径。
7. 月球车在重新规划线路时，会尽可能加快速度，即如果接下来的n秒都是同向运动，则会在接下来的1秒内会走完n格，即加速到n格/秒。
8. 每个月球车线程会每秒将当前位置加入控制中心的队列中。控制中心线程每500ms消费各个月球车对应的队列，并将月球车当前位置和预测2秒后的位置打印出来。
9. Main对象为入口点，启动1个控制中心线程和5个月球车线程。


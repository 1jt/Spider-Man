# 技术文档

## 一. 测试数据集生成

### 1. RandomDistribution

用于生成满足随机分布的随机数的类，主要用于后续文档生成。

#### 输入

- `numPairs`：键值对的个数
- `maxvolume`：最大值对应的Volume

#### 输出

- `distribution`：一个长度为`numPairs`的数列，表示生成的随机分布
    > 控制第一个数为最大值，方便后续查询

#### 使用示例

```java
RandomDistribution rd = new RandomDistribution(1024, 16);
// print rd.distribution
for (int i = 0; i < rd.distribution.size(); i++) {
    System.out.print(rd.distribution.get(i) + " ");
}
```

### 2. ZipfDistribution

用于生成满足随机分布的随机数的类，主要用于后续文档生成。

#### 输入

- `numPairs`：键值对的个数
- `numKeys`：期望的关键词数量（只少不多）

    > 因此平均volume为`numPairs / numKeys`

#### 输出

- `distribution`：一个长度为`numPairs`的数列，表示生成的随机分布

#### 使用示例

```java
ZipfDistribution zipf = new ZipfDistribution(1024, 128);
// print zipf.distribution
for (int i = 0; i < zipf.distribution.size(); i++) {
    System.out.print(zipf.distribution.get(i) + " ");
}
```

### 3. KeyValueGenerator

用于生成满足随机分布的随机数的类，主要用于后续文档生成。

#### 输入

- `model`：选择模式 (z/r)
- `numPairs`：键值对的个数
- `numKeys`：期望的关键词数量 (zipf分布)
- `maxvolume`：最大值对应的Volume (random分布)
- `filename`：输出文件名


#### 输出

- 根据输入参数，在对应文件夹生成的不同分布数据集
  > Zipf分布在 DB_zipf 文件夹下
  > Random分布在 DB_random 文件夹下

#### 使用示例

```java
//  char model = 'r'; // 'r' for random, 'z' for zipf
    char model = 'z';
    int numPairs = 1024 * 2 * 2 * 2 * 2 * 2; // 键值对的总数
    int numKeys = numPairs/8; // 键的数量
    int maxVolume = 16; // 值的最大数量
    String fileName = "Zipf_15.txt";
```

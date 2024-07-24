# 技术文档

## 一. 测试数据集生成（DataGen）

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

根据不同分布和输入参数的不同，生成符合该分布的数据集。

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

### 4. Shuffle

将生成的数据集打乱，用于后续代码测试

#### 输入

- `model`：选择模式 (z/r)
- `filename`：输出文件名

#### 输出

- 根据输入参数，在对应文件夹放入的不同打乱后的数据集
  > Zipf分布在 DB_zipf_shuffle 文件夹下
  > Random分布在 DB_random_shuffle 文件夹下
  > 文件名与原始文件相同

#### 使用示例

```java
//  char model = 'r'; // 'r' for random, 'z' for zipf
    char model = 'z';
    String fileName = "Zipf_10.txt";
```

## 二. 密码学工具（Tools）

### 1. Hash

提供各种哈希函数的接口

#### 使用说明

- `hash64`：64-bit hash function // 输入为 long
- `reduce`：reduce hash value to $[0,n)$
- `Get_SHA_256` // 以下输入均为 byte[]
  - 输出 byte[32]
- `Get_MD5`
  - 输出 byte[16]
- `Get_Sha_128`
  - 输出 byte[16]

### 2. tool

提供各种常用工具函数的接口

#### 使用说明

- `bytesToLong`：将 byte[] 转换为 long
- `longToBytes`：将 long 转换为 byte[]
- `DecimalConversion(int inNum, int index, int level)`： 
  - 将一个十进制数inNum转换为index进制，并将转换后的每一位数字存储在一个整数数组中，数组长度为level

### 3. AESUtil

#### 使用说明

- `AESUtil.encrypt(key, value)`
  - 加密结果是 128 bits（16 bytes）
- `AESUtil.decrypt(key, encrypted)`

#### 使用示例

```java
byte[] key = Hash.Get_SHA_256("123".getBytes());
byte[] value = "hello".getBytes();
for (byte b : value) {
    System.out.print(Integer.toHexString(b & 0xff) + " ");
}

// test AESUtil.encrypt
System.out.println();
byte[] encrypted = AESUtil.encrypt(key, value);
for (byte b : encrypted) {
    System.out.print(Integer.toHexString(b & 0xff) + " ");
}

// test AESUtil.decrypt
System.out.println();
byte[] decrypted = AESUtil.decrypt(key, encrypted);
for (byte b : decrypted) {
    System.out.print(Integer.toHexString(b & 0xff) + " ");
}
```

### 4. Cuckoo_Hash

#### 使用说明

- `K_d` ： 以 GGM 结构为基础的哈希函数的密钥
  - `Get_K_d()`
- `K_e` ： AES 加密的密钥
  - `Get_K_e()`
- `cuckoo_table` ：存储原始数据
- `EMM` ：存储加密数据
  - `Get_EMM()`
- `table_size` ：哈希表大小 (单张大小)
  - `Get_Table_Size()` // 元素数量的1.3倍
- `level` : GGM 结构的层数（即maxVol的对数）
  - `Get_Level()`
- `Stash` : 存储溢出数据
  - `Get_Stash()`
- `leave_map` : 记录存入的元素，可以避免重复计算
  - `Leave_Map_Clear()`
- `Setup(KV[] kv_list, int level)` ：建立哈希表
  - 将 kv_list 中的数据插入哈希表，level 为哈希表的层数（即maxVol的对数）
- `InsertEntry(int index,KV[] kv_list)` ：插入数据
  - 将 kv_list 中索引号为 index 的数据插入哈希表中
- `Get(int index` ：查询数据
  - 查询 EMM 中索引号为 index 的数据（解密后）

#### 使用示例

```java
KV[] kv_list = new KV[10];
for (int i = 0; i < kv_list.length; i++) {
    kv_list[i] = new KV();
    kv_list[i].key = "key" + i;
    kv_list[i].value = "value" + i;
    kv_list[i].counter = i;
}

int MAX_VOLUME_LENGTH = (int) Math.pow(2, 5); // 随便定的
int CUCKOO_LEVEL = (int) Math.ceil(Math.log(MAX_VOLUME_LENGTH) / Math.log(2.0));//GGM Tree level for cuckoo hash

Cuckoo_Hash cuckoo = new Cuckoo_Hash();
cuckoo.Setup(kv_list, CUCKOO_LEVEL);
for (int i = 0; i < cuckoo.Get_Table_Size()*2; i++) {
    System.out.println(cuckoo.Get(i));
}
```


## 三. CCS'19 方案一（dprfMM）

### 1. GGM

#### 使用说明

- `map`：GGM 映射表
- `clear()`：清空映射表
- `Tri_GGM_Path`：三叉 GGM 映射函数
- `Doub_GGM_Path`：二叉 GGM 映射函数
- `Map2Range(byte[] hash,int capacity,int index)`
  - 利用 hash 值（8字节）确定在哈希表中的位置，哈希表大小为 capacity，index 为第几张哈希表


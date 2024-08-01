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
- 提供了两种生成文件的方式
  - `txt` 方式：将键值对用可直观的方式写入文本文件中
  - `Serializable` 方式：将键值对序列化后写入文件，文件后缀可以为`.ser`，`.dat`等（具体参考后续 SerialData）

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
- `int[] DecimalConversion(int inNum, int index, int level)`： 
  - 将一个十进制数inNum转换为index进制，并将转换后的每一位数字存储在一个整数数组中，数组长度为level
- `int[] Get_Total_Max_Num(String filename)`：
  - 从文件名中获取数据集的总数和最大值

### 3. AESUtil

提供 AES 加密解密的接口

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

提供基于 GGM 结构的布谷鸟过滤器的接口

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

### 5. SerialData

提供序列化 写入\读取 数据的接口

#### 使用说明

- `Serial_Raw_Out(ArrayList<Integer> distribution,String fileName)`
  - 被键值对生成器调用，将原始数据集序列化写入文件
- `Serial_Raw_In(String fileName)`
  - 从文件中序列化读取原始数据集
  - 以 KV[] 的形式返回

#### 使用示例

```java
KV[] kvs = Serial_Raw_In("DB_random/Random_10_4.ser");
// print kvs
assert kvs != null;
for (KV kv : kvs) {
    System.out.println(kv.key + " " + kv.value);
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

### 2. dprfMM

#### 使用说明

- `dprfMM(int numPairs, int maxVolume, String filename)`
  - 构造函数，建立 Cuckoo Filter
    - 第二种构造方式：`dprfMM(String filename)` : 从文件名中获取参数（非通用）
  - `Setup(String filename)`
    - 由构造函数调用，建立 Cuckoo Filter
- `ArrayList<String> DprfQuery(String search_key)`
  - 查询函数，返回查询结果
  - `byte[] GenSearchToken(String search_key)`
    - 由查询函数，生成查询令牌
    - 提供静态版本，供外部调用：`byte[] GenSearchToken(String search_key,long K_d)`
  - `ArrayList<byte[]> Query_Cuckoo(byte[] token)`
    - 由查询函数调用，返回服务器查询结果
    - 提供静态版本，供外部调用：`ArrayList<byte[]> Query_Cuckoo(byte[] token,Cuckoo_Hash cuckoo,int MAX_VOLUME_LENGTH)`
  - `ArrayList<String> DecryptResult(ArrayList<byte[]> ServerResult, String search_key)`
    - 由查询函数调用，解密服务器查询结果
  - `SearchStash(String search_key, ArrayList<String> ClientResult)`
    - 由查询函数调用，查询溢出数据

#### 使用示例

```java
// test dprfMM
System.out.println("----------------------------------------------test dprfMM-------------------------------------------");
String filename = "DB_random/Random_10_4.ser";
int[] params = tool.Get_Total_Max_Num(filename);

// 不要求中间结果的情况下，以下两行已经包含了所有操作
// dprfMM dprf = new dprfMM(filename); // 简化构造
dprfMM dprf = new dprfMM(params[0],params[1],filename);
ArrayList<String> result = dprf.DprfQuery("Key0");

byte[] tk_key = dprfMM.GenSearchToken("Key0", Cuckoo_Hash.Get_K_d());
System.out.println("\nGenerate token by static method:\n" + Arrays.toString(tk_key));

ArrayList<byte[]> ServerResult = dprfMM.Query_Cuckoo(tk_key,dprf.cuckoo,params[1]);
System.out.println("\nCiphertext and corresponding plaintext returned by the server: ");
for (byte[] ciphertext : ServerResult) {
    System.out.print(Arrays.toString(ciphertext) + " ");
    System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
}

System.out.println("\nFinal Result: ");
for (String s : result) {
    System.out.print(s + " ");
}
```

## 四.共享树型方案（NewDVH）

### 1. 对象类

描述该方案中所使用的所有对象信息。

#### 使用说明

- `MMpoint`：包含（x,y）坐标，以及相应的构造方法。

- `TreeNode`：节点对象，包含数据`data`，左孩子节点指向，右孩子节点指向，节点位置`id`(MMPiont类型)，以及相应的构造方法。
- `NodeSet`：存储对象，包含`TreeNode`以及`MMPoint`，其中`MMPoint`数据与`TreeNode.id`相等，目的是在节点未知情况下判断该位置是否有节点存在，并包含相应的构造方法。

### 2. 工具类

提供方案所使用的工具类函数，以及方案主体部分代码。

#### 使用说明

- `NewDVH_Tool`：继承`Tools`，提供后续使用的各种工具函数，新增如下
  
  - `Size` :输入：文件路径，输出：`size`值。功能：计算初始化算法中根节点的数目-`size`，该值客户端和服务器都可见。
  
    ```java
    public static int Size(String filePath) throws IOException {
        int size;
        int n = Tools.CalculateNumberOfDBEntries(filePath);
        double c = 0.125;
        size = (int) (c * n / 8);
        return size;
    }
    ```
  
    其中`c`作为隐私参数。可以在此算法中，通过调整`c`值，控制查询通信开销以及隐私暴露大小，`c`越大，查询通信开销越小，隐私暴露越多。
  
  - `FindNode`：输入：`x`、`y`、数据集`Position`，输出：`TreeNode`型节点信息。功能：
  
  - `UpList`：输入：，输出：。功能：
  
  - `AddUpList`：输入：，输出：。功能：
  
- `Setup_NewDVH`：方案的初始化算法，包含静态变量`Position`以及主题函数`Test`，其中`Position`包含所有结构化的数据信息，`Test`包括方案具体流程。
  - `Position`：
  - `Test`：输入：，输出：。功能：
  
- `Update_Query_NewDVH `：执行更新方案第一步：对目标关键字进行查询，包含静态变量`List`以及主体函数`Run`，其中`List`记录查询时遍历所走的`pos`序列，用于生成随机更新队列。
  - `List`：
  - `Run`：输入：，输出：。功能：
  
- `Update_NewDVH`：方案的更新算法。包括两种更新方案：`DeleteUpdate`删除算法，以及`AddUpdate`更新算法。
  - `DeleteUpdate`：输入：，输出：。功能：
  - `AddUpdate`：输入：，输出：。功能：

### 3. 测试类

提供针对NewDVH方案的测试函数，可以在此提供不同的数据库，直接测试方案的各种开销。

- `UpdateTest_NewDVH`：
- `NewDVH_Test`：












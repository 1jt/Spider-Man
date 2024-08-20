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

### 4. Shuffle_txt

将生成的数据集打乱，用于后续代码测试（txt版本）

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

### 5. Shuffle_ser

将生成的数据集打乱，用于后续代码测试（Serializable版本）

#### 输入

- `filename`：输出文件名

#### 输出

- 根据输入参数，在对应文件夹放入的不同打乱后的数据集（都在Shuffle 大文件夹下）
  > Zipf分布在 DB_zipf_shuffle 文件夹下
  > Random分布在 DB_random_shuffle 文件夹下
  > 文件名与原始文件相同

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
- `KL[] KV_2_KL(KV[] kv_list)`
  - 从数据集文件中获取每个关键词对应值的数量

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
- `Serial_Raw_Out(KV[] kv_list,String fileName)`
  - 如果已经生成 KV[]，可以直接写入文件
- `Serial_Raw_In(String fileName)`
  - 从文件中序列化读取原始数据集
  - 以 KV[] 的形式返回
- `Serial_DB_Out`
  - `(dprfMM dprf,String fileName)` // 用于 dprfMM
  - `(dpMM dp,String fileName)` // 用于 dpMM
  - `(VHDSSE vhdsse, String fileName)` // 用于 VHDSSE
  - `(chFB chfb,String fileName)` // 用于 chFB
- `Serial_dprfMM_In(String fileName)`
  - 从文件中序列化读取 dprfMM 数据集
- `Serial_dpMM_In(String fileName)`
  - 从文件中序列化读取 dpMM 数据集
- `Serial_VHDSSE_In(String fileName)`
  - 从文件中序列化读取 VHDSSE 数据集
- `Serial_chFB_In(String fileName)`
  - 从文件中序列化读取 chFB 数据集

#### 使用示例

```java
KV[] kvs = Serial_Raw_In("DB_random/Random_10_4.ser");
// print kvs
assert kvs != null;
for (KV kv : kvs) {
    System.out.println(kv.key + " " + kv.value);
}

// 写入
dprfMM dprf = new dprfMM("Zipf_9_118.ser");
SerialData.Serial_DB_Out("Zipf_9_118.ser");

// 读出
dpMM dp = SerialData.Serial_dpMM_In("Zipf_15_3688.ser");
ArrayList<String> result = dp.DpQuery("Key1049");

System.out.println("\nFinal Result: ");
for (String s : result) {
    System.out.print(s + " ");
}
```

## 三. dprfMM

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

## 四. dpMM

### 1. Laplace

#### 使用说明

- `sensitivity`：敏感度
- `epsilon`：隐私预算
- `double getNoise(double param)`
  - 自定义参数
- `double getNoise()`
  - 默认参数

### 2. dpMM

#### 使用说明

- `dpMM(int numPairs, int maxVolume, String filename)`
  - 构造函数，建立 Cuckoo Filter
    - 第二种构造方式：`dpMM(String filename)` : 从文件名中获取参数（非通用）
  - `Setup(String filename)`
    - 由构造函数调用，建立 Cuckoo Filter
- `ArrayList<String> DpQuery(String search_key)`
  - 查询函数，返回查询结果
  - `byte[] GenSearchToken(String search_key)`
    - 由查询函数调用，生成查询令牌
    - 提供静态版本参考 dprfMM
  - `ArrayList<byte[]> Query_l_key(byte[] token)`
    - 由查询函数调用，服务器查询 $l(key)$ 函数
    - 提供静态版本，供外部调用：`ArrayList<byte[]> Query_l_key(byte[] token,Cuckoo_Hash CT)`
    - `ArrayList<byte[]>` 只是为了保持接口一致，实际上只有两个元素
  - `int Decrypt_l_key(ArrayList<byte[]> ServerResult, String search_key)`
    - 由查询函数调用，客户端解密 $l(key)$
    - 如果不在哈希表中，返回 -1
  - `int SearchCTStash(String search_key)`
    - $l(key)$ 不在哈希表中时，查询CT的stash
  - `Add_Noise(int l_key)`
    - 由查询函数调用，添加噪声
    - 提供静态版本，可以自定义 $l(\lambda)$: `int Add_Noise(int l_key, int l_lambda)`
  - `ArrayList<byte[]> Query_Data(byte[] token,int l_key)`
    - 由查询函数调用，返回服务器查询结果
    - 提供静态版本，供外部调用：`ArrayList<byte[]> Query_Data(byte[] token,int l_key,Cuckoo_Hash Data)`
  - 解密函数参考 dprfMM
    - `ArrayList<String> DecryptResult(ArrayList<byte[]> ServerResult, String search_key)`
  - `SearchDataStash(String search_key, ArrayList<String> ClientResult)`
    - 由查询函数调用，查询溢出数据

#### 使用示例

  ```java
  // test dpMM
System.out.println("----------------------------------------------test dpMM-------------------------------------------");
String filename = "DB_zipf/Zipf_15_3688.ser";
dpMM dp = new dpMM(filename);
ArrayList<String> result = dp.DpQuery("Key1049");

System.out.println("\nFinal Result: ");
for (String s : result) {
    System.out.print(s + " ");
}
// 测试token
byte[] tk_key = dprfMM.GenSearchToken("Key45", Cuckoo_Hash.Get_K_d());
System.out.println("\nGenerate token by static method:\n" + Arrays.toString(tk_key));
// 测试服务器查询l(key)
ArrayList<byte[]> l_key = dpMM.Query_l_key(tk_key, dp.CT);
System.out.println("\nl(key) returned by the server: ");
for (byte[] ciphertext : l_key) {
    System.out.print(Arrays.toString(ciphertext) + " ");
    System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
}
// 测试服务器返回结果
ArrayList<byte[]> ServerResult = dpMM.Query_Data(tk_key,10,dp.Data);
System.out.println("\nCiphertext and corresponding plaintext returned by the server: ");
for (byte[] ciphertext : ServerResult) {
    System.out.print(Arrays.toString(ciphertext) + " ");
    System.out.println(new String(AESUtil.decrypt(Cuckoo_Hash.Get_K_e(), ciphertext)));
}
  ```

## 四. VHDSSE

### 1. VHDSSE

#### 使用说明

- `VHDSSE(int numPairs, int maxVolume, String filename)`
  - 构造函数，建立 VHDSSE 方案
    - 第二种构造方式：`VHDSSE(String filename)` : 从文件名中获取参数（非通用）
  - `int getMin()`
    - 由构造函数调用，计算参数`min`的值
  - `Setup(String filename)`
    - 由构造函数调用，建立 Cuckoo Filter
    - `dprfMM Setup(KV[] kv_list)`: 多态函数，根据数据集建立 dprfMM 方案
    - `byte[][] EncryptEMM(byte[] K, ArrayList<KV> Key_Value)`
      - 由Setup函数调用，加密 stash 和 buf
    - `int f(int n)`
      - $f(n) = O(\log n)$,用于计算stash的大小，系数暂设为2
- `ArrayList<String> VHDSSE_Query(String search_key)`
  - 查询函数，返回查询结果
  - `Search_stash_buf(String search_key, ArrayList<String> ClientResult,byte[][] stash,byte[][] buf)`
    - 由查询函数调用，用于客户端查询 stash 和 buf
  - `ArrayList<String> Judge_result(ArrayList<String> result)`
    - 由查询函数调用，判断结果并进行错误校验
- `byte[] GenSearchToken(String search_key)`
  - 由查询函数调用，生成查询令牌
  - 提供静态版本参考 dprfMM
- `ArrayList<byte[]> VHDSSE_Query_Server(String search_key)`
  - 返回服务器查询结果（密文）
- `void Update(KV kv)`
  - 更新函数，用于更新数据集
  - `ArrayList<dprfMM> Find_EDB(int DATA_SIZE)`
    - 根据数据集大小，提取需要返回的数据库
  - `void Get_stash_buf(ArrayList<KV> EDB_db)`
    - 提取stash 和 buf中的数据
  - `ArrayList<KV> Neutralize_add_del(ArrayList<KV> EDB_data)`
    - 中和添加和删除
- `void Update()`
  - 用于多次更新数据集
  - 操作为`add`和`del`
  - 键值对和操作任意一项为`q`,退出
  - 提供是否保存修改的选项
    - 如果保存，将修改后的数据集写入文件

#### 使用示例

```java
// 初始化方案并写入文件
System.out.println("----------------------------------------------test VHDSSE-------------------------------------------");
String filename = "Shuffle/DB_zipf/Zipf_9_117.ser";
int[] params = tool.Get_Total_Max_Num(filename);
VHDSSE vhdsse = new VHDSSE(params[0],params[1],filename);
SerialData.Serial_DB_Out(vhdsse,filename.split("/")[2]);

// 序列化读取 + 更新 + 查询
VHDSSE vh = SerialData.Serial_VHDSSE_In("Zipf_9_117.ser");
assert vh != null;
vh.Update();
ArrayList<String> result = vh.VHDSSE_Query("Key10");
System.out.println("\nFinal Result: ");
for (String s : result) {
    System.out.print(s + " ");
}
```

## 五. 2chFB

### 1. TreeNode

#### 使用说明

- `TreeNode()`：默认构造函数
- `TreeNode(T data)`：构建数据类型为 T 的节点
- `T getData()`：返回节点数据
- `setData(T data)`：设置节点数据
- `TreeNode<T> getLeft()`：返回左孩子节点
- `setLeft(TreeNode<T> left)`：设置左孩子节点
- `TreeNode<T> getRight()`：返回右孩子节点
- `setRight(TreeNode<T> right)`：设置右孩子节点
- `toString()`: 重写 toString 方法（T类型的数据也要有对应的 toString）

### 2. TreeOperation

#### 使用说明

- `static TreeNode createFullBinaryTree(int depth)`：创建一颗高度为depth 空的 满二叉树
- `static int getTreeDepth(TreeNode<KV> root)`: 获取树的深度（根节点的深度为 0）
- `static void show(TreeNode<KV> root)`：打印树的结构（尤其适用于KV类型，其它类型还没测试）
  - `static void writeArray(TreeNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth)` : 配合show函数使用

### 3. TwoChoiceHash

#### 使用说明

- `B` : 存储加密数据库
- `TwoChoiceHash(KV[] kv_list, int s, int h)` : 构造函数，建立二选哈希
  - `kv_list` : 需要填入的数据集
  - `s` : 二叉树数量
  - `h` : 二叉树高度
- `Setup(KV[] kv_list,TreeNode<KV>[] forest)` : 建立函数
  - `forest` : 存储明文
  - `InsertAll(KV[] kv_list,TreeNode<KV>[] forest)` : 插入函数,插入所有数据，由Setup调用
    - `Insert(KV kv, TreeNode<KV> node,int depth,int loc)` : 将KV 沿着路径 loc 插入到 node 深为 depth 的节点
- `int G(KV kv, int index)` : $G_{\kappa}(j || 0/1)$
  - `index` : 0 或 1
  - `static int Map2Range(byte[] hash,int capacity)` : 将 hash 值映射到 [0,capacity) 的范围，由 G 调用
- `int CheckDeepest(int index, TreeNode root)` : 计算树的非空最深处
- `Encry_B(TreeNode<KV> root_p,TreeNode<byte[]> root_c)` : 将明文树（root_p）加密为密文树 （root_c）
- `Query(int tree_index, int loc, ArrayList<byte[]> result)` : 查询函数
  - `tree_index` : 二叉树编号
  - `loc` : bin的相对位置
  - `result` : 查询结果
- `WriteBack(String key,ArrayList<String> v,ArrayList<KV> unwanted,Set<Integer> bin)` : 写回函数
  - `key` : 查询关键字
  - `v` : 查询结果
  - `unwanted` :未用到的的数据
  - `bin` : 查询结果的位置
  - `TreeNode<byte[]> getTreeNode(int G_0, int dep_0)` : 获取密文树的节点,由 WriteBack 调用

### 4. chFB

#### 使用说明

- `chFB(int numPairs, int maxVolume, String filename)` : 构造函数，建立 chFB 方案
  - 第二种构造方式：`chFB(String filename)` : 从文件名中获取参数（非通用）
- `Update(String key,ArrayList<String> v,String op)` : 更新函数
  - `key` : 更新关键字
  - `v` : 更新向量
  - `op` : 操作类型
- `ArrayList<String> Query(String key)` : 查询函数
  - `key` : 查询关键字
- `Query_Update()` ：全自动测试函数
  - 用吧，一用一个不吱声

#### 使用示例
  
```java
// test chFB
// 生成方案并序列化写入文件
System.out.println("----------------------------------------------test chFB-------------------------------------------");
String filename = "Shuffle/DB_zipf/Zipf_9_109.ser";
chFB chfb = new chFB(filename);
SerialData.Serial_DB_Out(chfb,filename.split("/")[2]);

// 序列化读出并运行
chFB chfb = SerialData.Serial_chFB_In("Zipf_9_109.ser");
assert chfb != null;
chfb.Query_Update();
```


## 六. 共享树型方案（NewDVH）

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

  - `FindNode`：输入：`x`、`y`、数据集`Position`，输出：`TreeNode`型节点信息。功能：判断所给数据集中，是否存在对应该位置的数据。

    ```java
    public static TreeNode FindNode(int x,int y,ArrayList<NodeSet>Position){
    
        for (int i = 0; i < Position.size(); i++) {
            int aid_x = Position.get(i).getPosition().getX();
            int aid_y = Position.get(i).getPosition().getY();
            if (aid_x == x && aid_y == y) {
                TreeNode node = Position.get(i).getNode();
                return node;
            }
        }
        return null;
    }
    ```

    存在数据符合坐标，输出节点信息，不存在返回`null`。

  - `UpList`：输入：需要输入目标`key` 、`value` 、查询`pos`序列、查询返回结果、尺寸`size`，输出：更新`pos`序列。功能：为更新算法计算寻找路径。

    - 在查询结果中找到目标`key`、`value`的位置，截取到该位置的`pos`序列。
    - 在该根节点的设定范围内，随机选取一根节点编号，作为更新所用根节点。
    - 根据所选取的更新根节点计算出到目标节点的更新路径。（由于更新根节点是完全随机的，路径顺序也是打乱的，服务器无法获取所更新目标节点和跟新根节点之间的关系）

  - `AddUpList`：输入：目标`key` 、`value `、查询`pos`序列、查询返回结果、尺寸`size`。输出：计算添加操作所用`pos`序列。功能：为添加操作找到数据库中目标节点位置。

    - 遍历查询结果中是否存在`dummy`值。若存在，截取到`dummy`之前的`pos`序列，
    - 若不存在将整个查询返回`pos`序列进行截取，并计算下一层左右孩子节点取向，即该关键字对应下一层的`pos`值。
    - 使用`UpList`相同计算方式，对截取到的序列进行计算，生成更新`pos`序列。
    - 无`dummy`值则直接输出，存在`dummy`值把计算到的下一层`pos`值加到队列末尾，再输出。（判断`dummy`值的存在，是为了减少不断更新导致空间利用率下降。不存在`dummy`值时，把最后一层`pos`值直接添加到队列末尾，而不对其进行随机化处理是要保证，所添加节点与其父节点的父子关系，确保查询算法的正确。）

- `Setup_NewDVH`：方案的初始化算法，包含静态变量`Position`以及主题函数`Test`，其中`Position`包含所有结构化的数据信息，`Test`包括方案具体流程。

  - `Position`：列表类型，所存数据结构为`NodeSet`型，用于存储所有节点及其位置信息。

  - `Test`：输入：文件地址。输出：结构化文件中所有数据，并将其存入`Position`中。功能：对KV型数据进行结构化处理编程节点型数据。

    - 根据读取到的key计算出根节点编号。

      ```java
      // 计算kappa
      kappa = HashKit.sha1(key + 0 + 1);//这里kappa只用1这一个路径
      
      // 计算root编号
      BigInteger tmp = new BigInteger(kappa, 16);
      root = tmp.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
      ```

    - 尝试将数据存储到根节点位置

      ```java
      // 如果对应根节点没有数据，则执行初始化操作
      if (roots[root].getData() == null) {
          roots[root].setData(key + "+" + value);
          //计算根节点在坐标轴的位置
          MMPoint NodePosition = new MMPoint(root, size - 1 - root);
          roots[root].setId(NodePosition);
          //位置和节点一起存入Nodeset中
          NodeSet node_temp = new NodeSet(NodePosition, roots[root]);
          Position.add(node_temp);
          continue;//开启下一次循环，读取下一个数据
      }
      ```

    - 无法放入，则计算每一层对应的pos值

      ```java
      String Pos = HashKit.sha384(kappa + root + count++);
      BigInteger tmp_2 = new BigInteger(Pos, 16);
      int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue(); // 计算往左还是往右
      ```

    - 根据pos值信息，确定左右孩子走向，计算出目标孩子节点(x,y)坐标，以左孩子节点为例。

      ```java
      int child_x = node_tmp.getId().getX();
      int child_y = node_tmp.getId().getY() + 1;
      MMPoint NodePosition = new MMPoint(child_x, child_y);
      ```

    - 判断该位置是否存在数据，不存在直接放入，存在将该节点作为当前节点继续计算下一层。

      ```java
      if (node_tmp.getLeft() == null) {
          for (int pos_num = 0; pos_num < Position.size(); pos_num++) {
              int aid_x = Position.get(pos_num).getPosition().getX();
              int aid_y = Position.get(pos_num).getPosition().getY();
              if (aid_x == child_x && aid_y == child_y) {
                  node_tmp.setLeft(Position.get(pos_num).getNode());
                  node_tmp = node_tmp.getLeft();
                  //遍历Position，xy坐标匹配成功表示该位置存在节点，但没有与node_temp建立父子关系
                  continue STOP;//跳出大循环，计算下一层位置
              }
          }
          //位置没人，建立一个节点，并存入Position中
          TreeNode<String> node_left = new TreeNode<String>(input);
          node_tmp.setLeft(node_left);
          node_tmp.setLeftId(NodePosition);
          NodeSet node_cash = new NodeSet(NodePosition, node_left);
          Position.add(node_cash);//插入成功，读取下一个数据
          break;
      } else {
          node_tmp = node_tmp.getLeft();//左节点存在数据，当前节点转变为其左孩子
      }
      ```

- `Update_Query_NewDVH `：执行更新方案第一步：对目标关键字进行查询，包含静态变量`List`以及主体函数`Run`，其中`List`记录查询时遍历所走的`pos`序列，用于生成随机更新队列。

  - `List`：列表型变量，存储查询过程中，遍历路径上的所有`pos`值，作为查询`pos`序列使用。

  - `Run(String key, int size, ArrayList<NodeSet> Position) `：输入：关键字`key`，尺寸`size`，以及数据库`Position`。输出：`Result`（列表型变量，存输所查询到的所有KV数据）。功能：对目标关键字进行查询，返回所有查询到的结果。

    - 计算`key`对应的根节点编号，并找到该节点

      ```java
      ArrayList<String> Result = new ArrayList<>();//Result中记录查询到的数据
      String kappa = HashKit.sha1(key + 0 + 1);
      BigInteger tmp_3 = new BigInteger(kappa, 16);
      int root = tmp_3.divideAndRemainder(BigInteger.valueOf(size))[1].intValue();
      list.clear();//由于是全局变量，每次使用前清理一下防止bug
      list.add(root);//序列第一个是根结点编号
      TreeNode<String> node_tmp = null;//记录当前节点
              for (int j = 0; j < Position.size(); j++) {//遍历数据库，找到对应编号为root的根节点
                  node_tmp = Position.get(j).getNode();
                  if (node_tmp.getId().getX() == root && node_tmp.getId().getY() == size - 1 - root) {
                      break;
                  }
              }
      ```

    - 计算每一层`pos`值，将其加入`List`中，根据`pos`值选择性遍历左右孩子节点，将节点中数据放入`Result`中。

      ```java
      Result.add(node_tmp.getData());//记录当前节点数据
      int x = node_tmp.getId().getX();
      int y = node_tmp.getId().getY();
      String Pos = HashKit.sha384(kappa + root + count++);//记录下一层pos值
      BigInteger tmp_2 = new BigInteger(Pos, 16);
      int pos = tmp_2.divideAndRemainder(BigInteger.valueOf(2))[1].intValue();
      list.add(pos);//添加pos值
      if (pos == 0){
          if (node_tmp.getLeft()!=null){//判断左孩子节点是否为空，不为空，直接迭代节点
              node_tmp = node_tmp.getLeft();
          }else {                       //为空，存在两种可能，1该位置真没有节点，2该位置有节点但没与当前节点建立父子关系
              y = y + 1;
              //在数据库中找到坐标为(x,y)的点
              node_tmp = NewDVH_Tool.FindNode(x,y,Position);
          }
      }
      ```

- `Update_NewDVH`：方案的更新算法。包括两种更新方案：`DeleteUpdate`删除算法，以及`AddUpdate`更新算法。

  - `DeleteUpdate(int size, String key, ArrayList<String> query_result, ArrayList<NodeSet> database)`：功能，使用dummy替换数据库中的（key，value）。（因为此节点信息可能正在被其他根节点使用，彻底删除会导致查询错误，所以使用dummy替换）

    - 客户端对key进行查询，获取其查询结果，然后从键盘读入要删除的value值。注：测试时间开销时，需将value设置为常量。

      ```java
      //从键盘读取要删除的值
      BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
      String dummy = "Dummy";
      System.out.println("输入要删除的value值");
      String value = index.readLine();
      ```

    - 客户端生成更新队列，发送给服务器，服务器找到目标后，进行dummy替换。

      ```java
      //通过查询获得的查询pos序列，生成更新pos序列
      ArrayList<Integer> query_list = Update_Query_NewDVH.list;
      ArrayList<Integer> update_list = NewDVH_Tool.UpList(value, key, query_list, query_result, size);
      //找到更新路径对应根节点，通过pos序列计算（x,y）坐标，直接找到目标节点
      int x = update_list.get(0), y = size - 1 - update_list.get(0);
      for (int j = 1; j < update_list.size(); j++) {
          if (update_list.get(j) == 0)
              y++;
          else x++;
      }
      //在节点库中找到目标节点后，执行删除
      for (NodeSet node : database) {
          if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
              node.getNode().setData(key + "+Value" + dummy);
          }
      }
      //清空更新序列，为下次更新做准备
      Update_Query_NewDVH.list.clear();
      ```

  - `AddUpdate(int size, String key, ArrayList<String> query_result, ArrayList<NodeSet> database)`：功能：将新增kv数据结构化并加入数据库之中。

    - 从键盘读取到KV数据

      ```java
      BufferedReader index = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("输入要添加的value值");
      String value = index.readLine();
      String data = key + "+Value" + value;//因为更新之前对目标进行过查询，知道key
      ```

    - 根据更新队列长度与查询队列长度之间的关系判断，是哪种类型的更新。

    - 若是第一种新增节点，根据AddUpList算法生成的队列找到队列倒数第二个位置的节点，初始化一个新节点，根据队列最后一位是0，还是1，为这两个节点建立父子关系。将value放入其中。

      ```java
      //使用更新pos序列，计算目标节点父节点(x,y)坐标
      int x = update_list.get(0), y = size - 1 - update_list.get(0);
      for (int j = 1; j < update_list.size() - 1; j++) {//这里只计算到其父节点，因为该位置没节点，要初始化一个
          if (update_list.get(j) == 0)
              y++;
          else x++;
      }
      //遍历数据库找到父亲节点
      for (NodeSet node : database) {
          if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
              TreeNode<String> new_node = new TreeNode<>(data);
              //更新序列最后一位表示目标几点和其父亲节点的关系
              if (update_list.get(update_list.size() - 1) == 0) {
                  y++;//左孩子，y++
                  node.getNode().setLeft(new_node);
              } else {
                  x++;  //右孩子，x++
                  node.getNode().setRight(new_node);
              }
              //设置该节点对应的所有数据，把他加入数据库里
              MMPoint new_point = new MMPoint(x, y);
              new_node.setId(new_point);
              NodeSet new_nodeset = new NodeSet(new_point, new_node);
              database.add(new_nodeset);
              break;
          }
      }
      ```

    - 若是第二种对dummy的覆盖，则直接根据AddUpList算法生成的队列找到目标节点，进行信息的覆盖。

      ```java
      //使用更新pos序列，计算目标节点(x,y)坐标
      int x = update_list.get(0), y = size - 1 - update_list.get(0);//
      for (int j = 1; j < update_list.size(); j++) {
          if (update_list.get(j) == 0)
              y++;
          else x++;
      }
      //找到对应节点，覆盖数据
      for (NodeSet node : database) {
          if (node.getNode().getId().getX() == x && node.getNode().getId().getY() == y) {
              node.getNode().setData(data);
          }
      }
      ```

### 3. 测试类

提供针对NewDVH方案的测试函数，可以在此提供不同的数据库，直接测试方案的各种开销。

- `UpdateTest_NewDVH`：提供更新功能上的测试，不包括时间测试。

  - `New_DVH_TestQuery(ArrayList<NodeSet> Position, int size)`测试查询功能。

    - `Query_num`记录实验轮数，测试中共进行100轮实验。

    - `Query_cycle_time`记录每轮实验查询关键字的个数，实验中每轮实验随机查询100个关键字。

    - `avage_num`记录每轮实验中真实数量暴露查询个数的最大值。

    - `max_num`记录所有实验的平均通信开销。

    - 实验选取的关键词在最大范围区间内选取，可根据数据库变化在此处更改范围。

      ```java
      Random rd = new Random();
      int index = rd.nextInt(120); //更新赋值
      query_key = "Key" + index;
      ```

  - `NewDVH_TestUpdate(ArrayList<NodeSet> Position,int size )`测试更新功能

    - 设计交互界面，可以不断进行添加、删除功能测试，每次更新之前会进行一次查询，保证功能无误。

- `NewDVH_Test`：提供整个方案的测试平台

  - element：记录文件路径。

  - size：计算存储结构中根节点尺寸。

  - 测试时间代码区域。

    ```java
    long startTime = System.nanoTime(); // 记录开始时间
    // 需要测试运行时间的代码段区间
    
    UpdateTest_NewDVH.New_DVH_TestQuery(Setup_NewDVH.Position,size);//测查询通信开销
    
    //测试运行时间代码段区间
    long endTime = System.nanoTime(); // 记录结束时间
    long executionTime = (endTime - startTime) / 1000000; // 计算代码段的运行时间（毫秒）
    
    System.out.println("代码段的运行时间为: " + executionTime + " 毫秒");
    
    
    UpdateTest_NewDVH.NewDVH_TestUpdate(Setup_NewDVH.Position,size); //测试更新功能实现
    ```











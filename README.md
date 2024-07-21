# NLKWhitelist

基于 RIAWhitelist 所实现的功能，以Velocity插件格式进行重写。

可追踪添加和移除的 Velocity 白名单插件。

## 截图


## 特性

* 理由询问
* 删除追踪
* 其它基本元数据

## 变化

* 基于 RIAWhitelist 所实现的功能，以Velocity插件格式进行重写；
* 不再支持BungeeCord;仅支持Velocity；
* 移除了RIA特色(指令中所有的/ria前缀,如 /riawladd 更变为 /wladd)；
* 实现了 RIAWhitelistVelocityReade 的功能；
* 修复了数据库删除数据后再添加时的抽象问题；
* 对 1.1-SNAPSHOT 的屎山进行重写，并堆叠出了另一个屎山.d

## 命令

### 添加白名单

```
/wladd <玩家ID> <担保人（若无请填UNKNOWN）> <审核批次号（担保或史前时代，请填 000）> <白名单添加备注>
```

### 移除白名单

```
/wlremove <玩家ID> <删除原因>
```

### 查询白名单

```
/wlquery <玩家ID>
```


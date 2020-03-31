##  SpaceCharts 安卓 图表库

一个简单易用的安卓图表库。

### 使用方式

#### 1.添加Gradle引用

```
repositories {
    jcenter()
}
implementation 'com.github.HanZhizhi:SpaceCharts:0.x'
```



### 1.饼状图PieChart

<img src="https://github.com/HanZhizhi/SpaceCharts/raw/master/gallery/PieChart.png" width="400px" height="350px" />

布局文件：

```xml
<com.space.charts.PieChart
    android:id="@+id/piechart_show"
    android:layout_width="300dp"
    android:layout_height="300dp"
    app:layout_constraintBottom_toTopOf="@+id/text_gallery"
    app:layout_constraintLeft_toLeftOf="@+id/text_gallery"
    android:layout_gravity="center_horizontal"
    app:background_color="#eeeeee"
    app:hollow="true"/>
```

Java代码：

```java
private PieChart pieChart;
pieChart=root.findViewById(R.id.piechart_show);

List<PieChartData> pieDatumBeans =new ArrayList<>();
pieDatumBeans.add(new PieChartData(strItemName,flotaValue)); ...

pieChart.setData(pieDatumBeans);
```

### 2.螺线图SpiralChart

<img src="https://github.com/HanZhizhi/SpaceCharts/raw/master/gallery/SpiralChart.png" width="500px" height="820px" />

布局文件

```xml
<com.space.charts.SpiralChart
    android:id="@+id/snail_chart"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    app:background_color="#f3f3f3"
    app:style="2"/>
```

Java代码：

```java
private SpiralChart snailChart;
snailChart=root.findViewById(R.id.snail_chart);

private List<SpiralChartData> data=new ArrayList<>();
data.add(new SpiralChartData(strName,intValue));

snailChart.setData(snailChartData);
```

## 3.折线图LineChart

布局文件
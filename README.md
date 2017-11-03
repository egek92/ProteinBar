## Download

[![](https://jitpack.io/v/egek92/ProteinBar.svg)](https://jitpack.io/#egek92/ProteinBar)
					
	

in your root `build.gradle` 

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}


```

in your app level `build.gradle` 

```gradle
dependencies {
    ...
    implementation 'com.github.egek92:ProteinBar:1.4'
}
```


## Usage Example

![alt text](https://user-images.githubusercontent.com/10755037/32389442-a83eb560-c0db-11e7-8095-98f44e87ce7e.png)
       
       
## Complete List of Methods

* `text("example")`  : set text 
* `textSize(12)` : set text size 
* `textStyle(textStyle.BOLD)` : define text style 
* `textColor(Color.parseColor("#FFFFFF"))` : set text color

* `duration(Protein.LENGTH_SHORT)`  : ProteinBar display duration

* `icon(R.drawable.icon)` : set icon

* `backgroundcolor(Color.parseColor("#FFFFFF"))` : set background color

* `centerText()` : allign text to center

alternatively you can use pre-made action ProteinBars

```
success()
info()
warning()
error()
```





       
       
       
       
  
